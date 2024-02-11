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
package fr.djaytan.mc.jrppb.spigot.adapter;

import static fr.djaytan.mc.jrppb.spigot.adapter.JobActionTypeSupportChecker.getSupportedJobActionTypes;
import static fr.djaytan.mc.jrppb.spigot.adapter.JobActionTypeSupportChecker.getUnsupportedJobActionTypes;
import static fr.djaytan.mc.jrppb.spigot.adapter.JobActionTypeSupportChecker.isSupportedJobActionType;
import static fr.djaytan.mc.jrppb.spigot.adapter.JobActionTypeSupportChecker.isUnsupportedJobActionType;
import static org.assertj.core.api.Assertions.assertThat;

import com.gamingmesh.jobs.container.ActionType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class JobActionTypeSupportCheckerTest {

  private static final Collection<ActionType> SUPPORTED_JOB_ACTION_TYPES =
      List.of(ActionType.PLACE, ActionType.BREAK, ActionType.TNTBREAK);
  private static final Collection<ActionType> UNSUPPORTED_JOB_ACTION_TYPES =
      List.of(
          ActionType.STRIPLOGS,
          ActionType.KILL,
          ActionType.MMKILL,
          ActionType.FISH,
          ActionType.CRAFT,
          ActionType.VTRADE,
          ActionType.SMELT,
          ActionType.BREW,
          ActionType.ENCHANT,
          ActionType.REPAIR,
          ActionType.BREED,
          ActionType.TAME,
          ActionType.DYE,
          ActionType.SHEAR,
          ActionType.MILK,
          ActionType.EXPLORE,
          ActionType.EAT,
          ActionType.CUSTOMKILL,
          ActionType.COLLECT,
          ActionType.BAKE);

  @Nested
  @DisplayName("isSupportedJobActionType()")
  class IsSupportedJobActionType {

    @ParameterizedTest
    @MethodSource
    void whenJobActionTypeIsSupported_shallReturnTrue(ActionType actionType) {
      assertThat(isSupportedJobActionType(actionType)).isTrue();
    }

    private static @NotNull Stream<Arguments> whenJobActionTypeIsSupported_shallReturnTrue() {
      return SUPPORTED_JOB_ACTION_TYPES.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void whenJobActionTypeIsUnsupported_shallReturnFalse(ActionType actionType) {
      assertThat(isSupportedJobActionType(actionType)).isFalse();
    }

    private static @NotNull Stream<Arguments> whenJobActionTypeIsUnsupported_shallReturnFalse() {
      return UNSUPPORTED_JOB_ACTION_TYPES.stream().map(Arguments::of);
    }
  }

  @Nested
  @DisplayName("isUnsupportedJobActionType()")
  class IsUnsupportedJobActionType {

    @ParameterizedTest
    @MethodSource
    void whenJobActionTypeIsSupported_shallReturnFalse(ActionType actionType) {
      assertThat(isUnsupportedJobActionType(actionType)).isFalse();
    }

    private static @NotNull Stream<Arguments> whenJobActionTypeIsSupported_shallReturnFalse() {
      return SUPPORTED_JOB_ACTION_TYPES.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void whenJobActionTypeIsUnsupported_shallReturnTrue(ActionType actionType) {
      assertThat(isUnsupportedJobActionType(actionType)).isTrue();
    }

    private static @NotNull Stream<Arguments> whenJobActionTypeIsUnsupported_shallReturnTrue() {
      return UNSUPPORTED_JOB_ACTION_TYPES.stream().map(Arguments::of);
    }
  }

  @Nested
  @DisplayName("getSupportedJobActionTypes()")
  class GetSupportedJobActionTypes {

    @Test
    void whenRetrievingSupportedJobActionTypes_shallReturnTheExpectedOnes() {
      assertThat(getSupportedJobActionTypes())
          .containsExactlyInAnyOrderElementsOf(SUPPORTED_JOB_ACTION_TYPES);
    }
  }

  @Nested
  @DisplayName("getUnsupportedJobActionTypes()")
  class GetUnsupportedJobActionTypes {

    @Test
    void whenRetrievingUnsupportedJobActionTypes_shallReturnTheExpectedOnes() {
      assertThat(getUnsupportedJobActionTypes())
          .containsExactlyInAnyOrderElementsOf(UNSUPPORTED_JOB_ACTION_TYPES);
    }
  }
}
