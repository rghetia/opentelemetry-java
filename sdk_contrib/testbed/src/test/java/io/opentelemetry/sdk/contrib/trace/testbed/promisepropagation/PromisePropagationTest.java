/*
 * Copyright 2019, OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.sdk.contrib.trace.testbed.promisepropagation;

import static com.google.common.truth.Truth.assertThat;

import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.contrib.trace.testbed.TestUtils;
import io.opentelemetry.sdk.trace.SpanData;
import io.opentelemetry.sdk.trace.export.InMemorySpanExporter;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.SpanId;
import io.opentelemetry.trace.Tracer;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Test;

/**
 * These tests are intended to simulate the kind of async models that are common in java async
 * frameworks.
 *
 * <p>For improved readability, ignore the phaser lines as those are there to ensure deterministic
 * execution for the tests without sleeps.
 */
public class PromisePropagationTest {
  private final InMemorySpanExporter exporter = InMemorySpanExporter.create();
  private final Tracer tracer = TestUtils.createTracerShim(exporter);
  private Phaser phaser;

  @Before
  public void before() {
    phaser = new Phaser();
  }

  @Test
  public void testPromiseCallback() {
    phaser.register(); // register test thread
    final AtomicReference<String> successResult1 = new AtomicReference<>();
    final AtomicReference<String> successResult2 = new AtomicReference<>();
    final AtomicReference<Throwable> errorResult = new AtomicReference<>();

    try (PromiseContext context = new PromiseContext(phaser, 3)) {
      Span parentSpan = tracer.spanBuilder("promises").startSpan();
      parentSpan.setAttribute("component", "example-promises");

      try (Scope ignored = tracer.withSpan(parentSpan)) {
        Promise<String> successPromise = new Promise<>(context, tracer);

        successPromise.onSuccess(
            new Promise.SuccessCallback<String>() {
              @Override
              public void accept(String s) {
                tracer.getCurrentSpan().addEvent("Promised 1 " + s);
                successResult1.set(s);
                phaser.arriveAndAwaitAdvance(); // result set
              }
            });
        successPromise.onSuccess(
            new Promise.SuccessCallback<String>() {
              @Override
              public void accept(String s) {
                tracer.getCurrentSpan().addEvent("Promised 2 " + s);
                successResult2.set(s);
                phaser.arriveAndAwaitAdvance(); // result set
              }
            });

        Promise<String> errorPromise = new Promise<>(context, tracer);

        errorPromise.onError(
            new Promise.ErrorCallback() {
              @Override
              public void accept(Throwable t) {
                errorResult.set(t);
                phaser.arriveAndAwaitAdvance(); // result set
              }
            });

        assertThat(exporter.getFinishedSpanItems().size()).isEqualTo(0);
        successPromise.success("success!");
        errorPromise.error(new Exception("some error."));
      } finally {
        parentSpan.end();
      }

      phaser.arriveAndAwaitAdvance(); // wait for results to be set
      assertThat(successResult1.get()).isEqualTo("success!");
      assertThat(successResult2.get()).isEqualTo("success!");
      assertThat(errorResult.get().getMessage()).isEqualTo("some error.");

      phaser.arriveAndAwaitAdvance(); // wait for traces to be reported

      List<SpanData> finished = exporter.getFinishedSpanItems();
      assertThat(finished.size()).isEqualTo(4);

      String component = "component";
      SpanData parentSpanProto = TestUtils.getOneByAttr(finished, component, "example-promises");
      assertThat(parentSpanProto).isNotNull();
      assertThat(parentSpanProto.getParentSpanId().isValid()).isFalse();
      List<SpanData> successSpans = TestUtils.getByAttr(finished, component, "success");
      assertThat(successSpans).hasSize(2);

      SpanId parentId = parentSpanProto.getSpanId();
      for (SpanData span : successSpans) {
        assertThat(span.getParentSpanId()).isEqualTo(parentId);
      }

      SpanData errorSpan = TestUtils.getOneByAttr(finished, component, "error");
      assertThat(errorSpan).isNotNull();
      assertThat(errorSpan.getParentSpanId()).isEqualTo(parentId);
    }
  }
}
