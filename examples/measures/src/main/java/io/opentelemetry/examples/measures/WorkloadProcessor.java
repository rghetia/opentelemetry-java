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

package io.opentelemetry.examples.measures;

import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.distributedcontext.DefaultDistributedContextManager;
import io.opentelemetry.metrics.AttachmentValue.AttachmentValueString;
import io.opentelemetry.metrics.LabelKey;
import io.opentelemetry.metrics.LabelValue;
import io.opentelemetry.metrics.MeasureDouble;
import io.opentelemetry.metrics.Meter;
import java.util.Collections;
import java.util.Random;

/**
 * Exports metrics about workload processing using measure.
 *
 * <p>Example metrics being exported:
 *
 * <pre>
 *   workload_processing_latency{workload_type="foo"} 6.7
 *   workload_processing_latency_count{workload_type="foo"} 6.7
 * </pre>
 */
public final class WorkloadProcessor {
  private static final LabelKey workloadTypeKey = LabelKey.create("workload_type", "");
  private final MeasureDouble workloadProcessing;
  private final MeasureDouble workloadProcessingSub; // TODO: Find a better name
  private final Meter meter;

  /** Constructs a WorkloadProcessor that measures processing latency. */
  public WorkloadProcessor() {
    this.meter = OpenTelemetry.getMeter();
    this.workloadProcessing =
        meter
            .measureDoubleBuilder("workload_processing_latency")
            .setDescription("measurement associated with workload processing")
            .setUnit("ms")
            .setLabelKeys(Collections.singletonList(workloadTypeKey))
            .build();
    LabelValue workloadTypeFoo = LabelValue.create("foo");
    this.workloadProcessingSub =
        this.workloadProcessing.getOrCreateSubMeasure(Collections.singletonList(workloadTypeFoo));
  }

  private static void doSomeWork() {
    try {
      Thread.sleep(new Random().nextInt(10) + 1);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /** processes workload. */
  public void processWorkload() {
    long startTime = System.nanoTime();
    doSomeWork();
    double processingTime = (System.nanoTime() - startTime) / 1e6;

    // Replace with actual span context.
    AttachmentValueString attachment = AttachmentValueString.create("span_context");
    workloadProcessingSub.record(
        processingTime,
        DefaultDistributedContextManager.getInstance().getCurrentContext(),
        attachment);

    // TODO: add example for each of the following cases.
    // Single Measure with only pre-defined label values for all label keys.
    // Single Measure with pre-defined values label value for some label keys.
    // Single Measure with dynamic values for the labels from distributed context.
    // Single Measure with dynamic values for the labels from distributed context
    //  with additional labels.
    // Batch Measure with only pre-defined label values for all label keys.
    // Batch Measure with pre-defined label values for some label keys.
    // Batch Measure with dynamic values for the labels from distributed context.
    // Batch Measure with dynamic values for the labels from distributed context
    //  with additional labels.

  }

  /**
   * Main launcher for the WorkloadProcessor.
   *
   * @param args args not required.
   */
  public static void main(String[] args) {
    WorkloadProcessor processor = new WorkloadProcessor();
    processor.processWorkload();
  }
}
