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

package io.opentelemetry.metrics;

import io.opentelemetry.distributedcontext.DistributedContext;
import java.util.Collections;
import java.util.List;
import javax.annotation.concurrent.ThreadSafe;

/**
 * The definition of a {@link MeasureBatch} that is taken by OpenTelemetry library.
 *
 * @since 0.1.0
 */
@ThreadSafe
public interface MeasureBatch {
  /**
   * Records list of measurements for each Measures associated with {@code MeasureBatch}.
   *
   * @param numbers the list of number that is recorded for each Measure associated with this {@code
   *     MeasureBatch}. Measurements must be in same order as the order in which measures were added
   *     to the batch.
   * @param distContext the distContext associated with the measurements.
   * @param attachmentValue the attachment value for exemplar.
   * @since 0.1.0
   */
  void record(
      List<Number> numbers, DistributedContext distContext, AttachmentValue attachmentValue);

  /** Builder class for the {@link MeasureBatch}. */
  interface Builder {
    /**
     * Adds a measure to a list of measures represented by this {@code MeasureBatch}.
     *
     * <p>Default value is {@link Collections#emptyList()}
     *
     * @param measure a measure to add.
     * @return this.
     */
    Builder addMeasure(Measure measure);

    /**
     * Builds and returns a {@code MeasureBatch} with list of measures.
     *
     * @return a {@code MeasureBatch} with the list of measures.
     */
    MeasureBatch build();
  }
}
