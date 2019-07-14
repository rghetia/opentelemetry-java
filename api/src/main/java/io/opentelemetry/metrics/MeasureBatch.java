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
   * @param measurements the list of measurements that is recorded for each Measure associated with
   *     this {@code MeasureBatch}.
   * @since 0.1.0
   */
  void record(List<Measurement> measurements, DistributedContext distContext);

  /** Builder class for the {@link Measure}. */
  interface Builder {
    /**
     * Sets the list of label keys for this {@code Measure}.
     *
     * <p>Default value is {@link Collections#emptyList()}
     *
     * @param measures the list of {@code Measure} to apply to this {@code MeasureBatch}.
     * @return this.
     */
    Builder addMeasures(List<Measure> measures);

    /**
     * Builds and returns a {@code MeasureBatch} with the desired options.
     *
     * @return a {@code MeasureBatch} with the desired options.
     */
    MeasureBatch build();
  }
}
