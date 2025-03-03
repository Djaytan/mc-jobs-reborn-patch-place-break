/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.mc.jrppb.core.config;

import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.NOMINAL_CONFIG_NAME_VALUE;
import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.randomInvalidConfigNameValue;
import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.randomValidConfigNameValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

final class ConfigNameTest {

  @Nested
  class WhenInstantiating {

    @Nested
    class SuccessCase {

      @Test
      void nominalValue() {
        assertInstantiationSuccess(NOMINAL_CONFIG_NAME_VALUE);
      }

      @Test
      void withLongestPossibleName() {
        assertInstantiationSuccess("e".repeat(32));
      }

      @Test
      void withShortestPossibleName() {
        assertInstantiationSuccess("e".repeat(3));
      }

      @Test
      void withHyphenInName() {
        assertInstantiationSuccess("config-name");
      }

      @Test
      void withUnderscoreInName() {
        assertInstantiationSuccess("config_name");
      }

      @RepeatedTest(100)
      void withRandomlyGeneratedValidValue() {
        assertInstantiationSuccess(randomValidConfigNameValue());
      }

      private static void assertInstantiationSuccess(@NotNull String configNameValue) {
        assertThat(new ConfigName(configNameValue).value()).isEqualTo(configNameValue);
      }
    }

    @Nested
    class FailureCase {

      @Test
      void withNameSizeJustAboveLimit() {
        assertInstantiationFailure("e".repeat(33));
      }

      @Test
      void withNameSizeJustBelowLimit() {
        assertInstantiationFailure("e".repeat(2));
      }

      @Test
      void withEmptyName() {
        assertInstantiationFailure("");
      }

      @Test
      void withBlankName() {
        assertInstantiationFailure(" ");
      }

      @Test
      void withBlankName_andValidLength() {
        assertInstantiationFailure("   ");
      }

      @RepeatedTest(100)
      void withRandomlyGeneratedInvalidValue() {
        assertInstantiationFailure(randomInvalidConfigNameValue());
      }

      private static void assertInstantiationFailure(@NotNull String configNameValue) {
        assertThatThrownBy(() -> new ConfigName(configNameValue))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("The config name '%s' is invalid", configNameValue);
      }
    }
  }
}
