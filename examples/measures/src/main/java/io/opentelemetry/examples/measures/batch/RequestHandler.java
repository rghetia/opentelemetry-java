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

package io.opentelemetry.examples.measures.batch;

import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.distributedcontext.DefaultDistributedContextManager;
import io.opentelemetry.metrics.AttachmentValue.AttachmentValueString;
import io.opentelemetry.metrics.LabelKey;
import io.opentelemetry.metrics.MeasureDouble;
import io.opentelemetry.metrics.MeasureLong;
import io.opentelemetry.metrics.Meter;
import java.util.Arrays;
import java.util.Random;

/**
 * Exports metrics about request handling using batched measure.
 *
 * <p>Example metrics being exported:
 *
 * <pre>
 *   workload_processing_latency{workload_type="foo"} 6.7
 *   workload_processing_latency_count{workload_type="foo"} 6.7
 * </pre>
 */
public final class RequestHandler {
  private static final LabelKey methodKey = LabelKey.create("method", "");
  private static final LabelKey hostKey = LabelKey.create("host", "");
  private static final LabelKey statusKey = LabelKey.create("status", "");
  private final MeasureLong requestSize;
  private final MeasureLong responseSize;
  private final MeasureDouble requestLatency;
  // private final MeasureBatch measureBatch;
  private final Meter meter;

  /** Constructs a RequestHandler that measures processing latency. */
  public RequestHandler() {
    this.meter = OpenTelemetry.getMeter();
    this.requestSize =
        meter
            .measureLongBuilder("request_size")
            .setDescription("size of the request")
            .setUnit("By")
            .setLabelKeys(Arrays.asList(methodKey, statusKey))
            .build();
    this.responseSize =
        meter
            .measureLongBuilder("response_size")
            .setDescription("size of the response")
            .setUnit("By")
            .setLabelKeys(Arrays.asList(methodKey, statusKey))
            .build();
    this.requestLatency =
        meter
            .measureDoubleBuilder("request_latency")
            .setDescription("latency of request processing")
            .setUnit("ms")
            .setLabelKeys(Arrays.asList(methodKey, statusKey, hostKey))
            .build();
    //    this.measureBatch =
    //        meter
    //            .measureBatchBuilder()
    //            .addMeasure(requestSize)
    //            .addMeasure(responseSize)
    //            .addMeasure(requestLatency)
    //            .build();
  }

  private static void doSomeWork() {
    try {
      Thread.sleep(new Random().nextInt(10) + 1);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /** processes workload. */
  public void handleRequest() {
    long startTime = System.nanoTime();
    doSomeWork();
    double processLatency = (System.nanoTime() - startTime) / 1e6;

    // Replace with actual span context.
    AttachmentValueString attachment = AttachmentValueString.create("span_context");
    requestSize.record(
        new Random().nextInt(10),
        DefaultDistributedContextManager.getInstance().getCurrentContext(),
        attachment);
    responseSize.record(
        new Random().nextInt(10),
        DefaultDistributedContextManager.getInstance().getCurrentContext(),
        attachment);
    requestLatency.record(
        processLatency,
        DefaultDistributedContextManager.getInstance().getCurrentContext(),
        attachment);
    //    measureBatch.record(
    //        Arrays.asList(reqSize, resSize, latency),
    //        DefaultDistributedContextManager.getInstance().getCurrentContext(),
    //        attachment);
  }

  /**
   * Main launcher for the RequestHandler.
   *
   * @param args args not required.
   */
  public static void main(String[] args) {
    RequestHandler handler = new RequestHandler();
    handler.handleRequest();
  }
}
