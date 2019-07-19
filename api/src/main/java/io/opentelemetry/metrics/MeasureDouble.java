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
import java.util.List;
import javax.annotation.concurrent.ThreadSafe;

/**
 * The definition of a {@link Measurement} that is taken by OpenTelemetry library.
 *
 * @since 0.1.0
 */
@ThreadSafe
public interface MeasureDouble extends Measure {
  /**
   * Records a measurement associated with {@code MeasureDouble}.
   *
   * @param value the value that is recorded for this {@code MeasureDouble}.
   * @param distContext the distContext associated with the value.
   * @param attachmentValue the attachment value for exemplar.
   * @since 0.1.0
   */
  void record(double value, DistributedContext distContext, AttachmentValue attachmentValue);

  /**
   * Creates a sub measure for a given set of {@code labelValues} if it is not already associated
   * with this {@code MeasureDouble}, else returns an existing sub measure.
   *
   * <p>It is recommended to keep a reference to the sub measure instead of always calling this
   * method for every operations.
   *
   * @param labelValues the list of label values. The number of label values must be the same to
   *     that of the label keys passed to {@link MeasureDouble.Builder#setLabelKeys(List)}.
   * @return a {@code MeasureDouble} the value of single measure.
   * @throws NullPointerException if {@code labelValues} is null OR any element of {@code
   *     labelValues} is null.
   * @throws IllegalArgumentException if number of {@code labelValues}s are not equal to the label
   *     keys.
   * @since 0.1.0
   */
  MeasureDouble getOrCreateSubMeasure(List<LabelValue> labelValues);

  /** Builder class for the {@link MeasureDouble}. */
  interface Builder extends Measure.Builder<Builder, MeasureDouble> {}
}
