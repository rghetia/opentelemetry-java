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
 * The definition of a {@link MeasureLong} that is taken by OpenTelemetry library.
 *
 * @since 0.1.0
 */
@ThreadSafe
public interface MeasureLong {
  /**
   * Records a measurement associated with {@code MeasureLong}.
   *
   * @param value the value that is recorded for this {@code MeasureLong}.
   * @param distContext the distContext associated with the value.
   * @param attachmentValue the attachment value for exemplar.
   * @since 0.1.0
   */
  void record(long value, DistributedContext distContext, AttachmentValue attachmentValue);

  /**
   * Creates a sub measure for a given set of {@code labelValues} if it is not already associated
   * with this {@code MeasureLong}, else returns an existing sub measure.
   *
   * <p>It is recommended to keep a reference to the sub measure instead of always calling this
   * method for every operations.
   *
   * @param labelValues the list of label values. The number of label values must be the same to
   *     that of the label keys passed to {@link MeasureLong.Builder#setLabelKeys(List)}.
   * @return a {@code MeasureLong} the value of single measure.
   * @throws NullPointerException if {@code labelValues} is null OR any element of {@code
   *     labelValues} is null.
   * @throws IllegalArgumentException if number of {@code labelValues}s are not equal to the label
   *     keys.
   * @since 0.1.0
   */
  MeasureLong getOrCreateSubMeasure(List<LabelValue> labelValues);

  /** Builder class for the {@link MeasureLong}. */
  interface Builder {
    /**
     * Sets the detailed description of the measure, used in documentation.
     *
     * <p>Default description is {@code ""}.
     *
     * @param description the detailed description of the {@code Measure}.
     * @return this.
     * @since 0.1.0
     */
    Builder setDescription(String description);

    /**
     * Sets the units in which {@code Measure} values are measured.
     *
     * <p>The suggested grammar for a unit is as follows:
     *
     * <ul>
     *   <li>Expression = Component { "." Component } {"/" Component };
     *   <li>Component = [ PREFIX ] UNIT [ Annotation ] | Annotation | "1";
     *   <li>Annotation = "{" NAME "}" ;
     * </ul>
     *
     * <p>For example, string “MBy{transmitted}/ms” stands for megabytes per milliseconds, and the
     * annotation transmitted inside {} is just a comment of the unit.
     *
     * <p>Default unit is {@code "1"}.
     *
     * @param unit the units in which {@code MeasureLong} values are measured.
     * @return this.
     * @since 0.1.0
     */
    Builder setUnit(String unit);

    /**
     * Sets the list of label keys for this {@code MeasureLong}.
     *
     * <p>Default value is {@link Collections#emptyList()}
     *
     * @param labelKeys the list of label keys for the Metric.
     * @return this.
     */
    Builder setLabelKeys(List<LabelKey> labelKeys);

    /**
     * Builds and returns a {@code MeasureLong} with the desired options.
     *
     * @return a {@code MeasureLong} with the desired options.
     */
    MeasureLong build();
  }
}
