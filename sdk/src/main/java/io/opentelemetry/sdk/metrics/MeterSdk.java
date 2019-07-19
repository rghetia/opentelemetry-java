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

package io.opentelemetry.sdk.metrics;

import io.opentelemetry.distributedcontext.DistributedContext;
import io.opentelemetry.metrics.CounterDouble;
import io.opentelemetry.metrics.CounterLong;
import io.opentelemetry.metrics.GaugeDouble;
import io.opentelemetry.metrics.GaugeLong;
import io.opentelemetry.metrics.MeasureBatch;
import io.opentelemetry.metrics.MeasureDouble;
import io.opentelemetry.metrics.MeasureLong;
import io.opentelemetry.metrics.Measurement;
import io.opentelemetry.metrics.Meter;
import io.opentelemetry.trace.SpanContext;
import java.util.List;

/** {@link MeterSdk} is SDK implementation of {@link Meter}. */
public class MeterSdk implements Meter {

  @Override
  public GaugeLong.Builder gaugeLongBuilder(String name) {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public GaugeDouble.Builder gaugeDoubleBuilder(String name) {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public CounterDouble.Builder counterDoubleBuilder(String name) {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public CounterLong.Builder counterLongBuilder(String name) {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public MeasureLong.Builder measureLongBuilder(String name) {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public MeasureDouble.Builder measureDoubleBuilder(String name) {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public MeasureBatch.Builder measureBatchBuilder() {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public void record(List<Measurement> measurements) {}

  @Override
  public void record(List<Measurement> measurements, DistributedContext distContext) {}

  @Override
  public void record(
      List<Measurement> measurements, DistributedContext distContext, SpanContext spanContext) {}
}
