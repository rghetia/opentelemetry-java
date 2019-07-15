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
 * The definition of a {@link Measurement} that is taken by OpenTelemetry library.
 *
 * @since 0.1.0
 */
@ThreadSafe
public interface Measure {
  /**
   * An enum that represents all the possible value types for a {@code Measure} or a {@code
   * io.opentelemetry.metrics.Measurement}.
   *
   * @since 0.1.0
   */
  enum Type {
    /**
     * Double typed {@code Measure} or {@code Measurement}.
     *
     * @since 0.1.0
     */
    DOUBLE,

    /**
     * Long typed {@code Measure} or {@code Measurement}.
     *
     * @since 0.1.0
     */
    LONG
  }

  /**
   * Returns a new {@link Measurement} for this {@code Measure}.
   *
   * @param value the corresponding {@code double} value for the {@code
   *     io.opentelemetry.metrics.Measurement}.
   * @return a new {@link Measurement} for this {@code Measure}.
   * @throws UnsupportedOperationException if the type is not {@link Measure.Type#DOUBLE}.
   */
  Measurement createDoubleMeasurement(double value);

  /**
   * Returns a new {@link Measurement} for this {@code Measure}.
   *
   * @param value the corresponding {@code long} value for the {@code
   *     io.opentelemetry.metrics.Measurement}.
   * @return a new {@link Measurement} for this {@code Measure}.
   * @throws UnsupportedOperationException if the type is not {@link Measure.Type#LONG}.
   */
  Measurement createLongMeasurement(long value);

  /**
   * Records a measurement associated with {@code Measure}.
   *
   * @param measurement the measurement that is recorded for this {@code Measure}.
   * @param distContext the distContext associated with the measurements.
   * @param attachmentValue the attachment value for exemplar.
   * @since 0.1.0
   */
  void record(
      Measurement measurement, DistributedContext distContext, AttachmentValue attachmentValue);

  /**
   * Creates a {@code SubMeasure} and returns a {@code SubMeasure} if the specified {@code
   * labelValues} is not already associated with this Measure, else returns an existing {@code
   * SubMeasure}.
   *
   * <p>It is recommended to keep a reference to the SubMeasure instead of always calling this
   * method for every operations.
   *
   * @param labelValues the list of label values. The number of label values must be the same to
   *     that of the label keys passed to {@link Measure.Builder#setLabelKeys(List)}.
   * @return a {@code SubMeasure} the value of single measure.
   * @throws NullPointerException if {@code labelValues} is null OR any element of {@code
   *     labelValues} is null.
   * @throws IllegalArgumentException if number of {@code labelValues}s are not equal to the label
   *     keys.
   * @since 0.1.0
   */
  SubMeasure getOrCreateSubMeasure(List<LabelValue> labelValues);

  /** SubMeasure class for {@code Measure} with fixed label values. */
  interface SubMeasure {
    /**
     * Records measurement against all aggregation associated with {@code SubMeasure}.
     *
     * @param measurement the measurement value that is recorded.
     * @since 0.1.0
     */
    void record(Measurement measurement);
  }

  /** Builder class for the {@link Measure}. */
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
     * @param unit the units in which {@code Measure} values are measured.
     * @return this.
     * @since 0.1.0
     */
    Builder setUnit(String unit);

    /**
     * Sets the {@code Type} corresponding to the underlying value of this {@code Measure}.
     *
     * <p>Default {@code Type} is {@link Type#DOUBLE}.
     *
     * @param type the
     * @return this.
     * @since 0.1.0
     */
    Builder setType(Type type);

    /**
     * Sets the list of label keys for this {@code Measure}.
     *
     * <p>Default value is {@link Collections#emptyList()}
     *
     * @param labelKeys the list of label keys for the Metric.
     * @return this.
     */
    Builder setLabelKeys(List<LabelKey> labelKeys);

    /**
     * Sets the list of label keys for this {@code Measure}.
     *
     * <p>Default value is {@link Collections#emptyList()}
     *
     * @param aggregations the list of aggregations to apply to this {@code Measure}.
     * @return this.
     */
    Builder addAggregations(List<Aggregation.Type> aggregations);

    /**
     * Builds and returns a {@code Measure} with the desired options.
     *
     * @return a {@code Measure} with the desired options.
     */
    Measure build();
  }
}
