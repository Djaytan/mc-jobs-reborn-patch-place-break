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
package fr.djaytan.mc.jrppb.spigot.adapter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.mc.jrppb.api.entities.BlockActionType;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ActionTypeConverterTest {

  private final ActionTypeConverter actionTypeConverter = new ActionTypeConverter();

  @Nested
  @DisplayName("convert()")
  class Convert {

    @ParameterizedTest
    @MethodSource
    void whenConvertingSupportedActionType_shallProcessSuccessfully(
        @NotNull ActionType actionType, @NotNull BlockActionType expectedValue) {
      assertThat(actionTypeConverter.convert(actionType)).isEqualTo(expectedValue);
    }

    private static @NotNull Stream<Arguments>
        whenConvertingSupportedActionType_shallProcessSuccessfully() {
      return Stream.of(
          arguments(ActionType.PLACE, BlockActionType.PLACE),
          arguments(ActionType.BREAK, BlockActionType.BREAK),
          arguments(ActionType.TNTBREAK, BlockActionType.TNTBREAK));
    }

    @ParameterizedTest
    @MethodSource
    void whenConvertingUnsupportedActionType_shallThrowException(@NotNull ActionType actionType) {
      assertThatThrownBy(() -> actionTypeConverter.convert(actionType))
          .isExactlyInstanceOf(IllegalArgumentException.class)
          .hasMessage(
              String.format(
                  "Unsupported job action type '%s' specified. Only the following ones are supported: [BREAK, TNTBREAK, PLACE]",
                  actionType.name()));
    }

    private static @NotNull Stream<Arguments>
        whenConvertingUnsupportedActionType_shallThrowException() {
      return Stream.of(
          arguments(ActionType.STRIPLOGS),
          arguments(ActionType.KILL),
          arguments(ActionType.MMKILL),
          arguments(ActionType.FISH),
          arguments(ActionType.CRAFT),
          arguments(ActionType.VTRADE),
          arguments(ActionType.SMELT),
          arguments(ActionType.BREW),
          arguments(ActionType.ENCHANT),
          arguments(ActionType.REPAIR),
          arguments(ActionType.BREED),
          arguments(ActionType.TAME),
          arguments(ActionType.DYE),
          arguments(ActionType.SHEAR),
          arguments(ActionType.MILK),
          arguments(ActionType.EXPLORE),
          arguments(ActionType.EAT),
          arguments(ActionType.CUSTOMKILL),
          arguments(ActionType.COLLECT),
          arguments(ActionType.BAKE));
    }
  }
}
