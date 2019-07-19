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
import io.opentelemetry.distributedcontext.Entry;
import io.opentelemetry.distributedcontext.EntryKey;
import io.opentelemetry.distributedcontext.EntryValue;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link DefaultMeter}. */
@RunWith(JUnit4.class)
public final class DefaultMeterTest {
  private static final Entry ENTRY =
      Entry.create(
          EntryKey.create("key"), EntryValue.create("value"), Entry.METADATA_UNLIMITED_PROPAGATION);

  private static final Meter defaultMeter = DefaultMeter.getInstance();

  private static final MeasureDouble MEASURE_DOUBLE =
      defaultMeter
          .measureDoubleBuilder("my measure")
          .setDescription("description")
          .setUnit("1")
          .build();

  private final DistributedContext distContext =
      new DistributedContext() {

        @Override
        public Collection<Entry> getEntries() {
          return Collections.singleton(ENTRY);
        }

        @Nullable
        @Override
        public EntryValue getEntryValue(EntryKey entryKey) {
          return EntryValue.create("value");
        }
      };

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void noopAddLongGauge_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    defaultMeter.gaugeLongBuilder(null);
  }

  @Test
  public void noopAddDoubleGauge_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    defaultMeter.gaugeDoubleBuilder(null);
  }

  @Test
  public void noopAddDoubleCumulative_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    defaultMeter.counterDoubleBuilder(null);
  }

  @Test
  public void noopAddLongCumulative_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    defaultMeter.counterLongBuilder(null);
  }

  // The NoopStatsRecorder should do nothing, so this test just checks that record doesn't throw an
  // exception.
  @Test
  public void noopStatsRecorder_Record() {
    MEASURE_DOUBLE.record(5, distContext, null);
  }
}
