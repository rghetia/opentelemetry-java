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

import javax.annotation.concurrent.Immutable;

/**
 * {@link Aggregation} is the process of combining a certain set of {@code Measurement}s for a given
 * {@code Measure} into the equivalent {@code Metric}.
 *
 * <p>{@link Aggregation} currently supports 4 types of basic aggregation:
 *
 * <ul>
 *   <li>Sum
 *   <li>Count
 *   <li>Distribution
 *   <li>LastValue
 * </ul>
 *
 * @since 0.1.0
 */
@Immutable
public abstract class Aggregation {

  private Aggregation() {}

  /**
   * Returns a {@code Type} corresponding to the underlying value of this {@code Aggregation}.
   *
   * @return the {@code Type} for the value of this {@code Aggregation}.
   * @since 0.1.0
   */
  public abstract Type getType();

  /**
   * An enum that represents all the possible value types for an {@code Aggregation}.
   *
   * @since 0.1.0
   */
  public enum Type {
    SUM,
    COUNT,
    DISTRIBUTION,
    LASTVALUE
  }
}
