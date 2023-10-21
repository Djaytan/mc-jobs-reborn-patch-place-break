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
package fr.djaytan.mc.jrppb.api.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BlockLocationTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withAllArgsConstructorAndNominalValues_shouldSuccess() {
      // Given
      String worldName = "world";
      int x = -54;
      int y = 67;
      int z = 4872;

      // When
      BlockLocation blockLocation = new BlockLocation(worldName, x, y, z);

      // Then
      assertAll(
          "Verification of returned values from getters",
          () -> assertThat(blockLocation.worldName()).isEqualTo(worldName),
          () -> assertThat(blockLocation.x()).isEqualTo(x),
          () -> assertThat(blockLocation.y()).isEqualTo(y),
          () -> assertThat(blockLocation.z()).isEqualTo(z));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    void withCopyConstructor_shouldCreateNewInstanceMatchingExpectedValue(
        @NotNull Vector givenDirection, @NotNull BlockLocation expectedValue) {
      // Given
      String worldName = "world";
      int initX = 1;
      int initY = -45;
      int initZ = -1;
      BlockLocation blockLocation = new BlockLocation(worldName, initX, initY, initZ);

      // When
      BlockLocation movedBlockLocation = BlockLocation.from(blockLocation, givenDirection);

      // Then
      assertAll(
          () -> assertThat(movedBlockLocation).isNotSameAs(blockLocation),
          () -> assertThat(movedBlockLocation).isEqualTo(expectedValue));
    }

    private static @NotNull Stream<Arguments>
        withCopyConstructor_shouldCreateNewInstanceMatchingExpectedValue() {
      return Stream.of(
          arguments(
              named("With nominal values", new Vector(5, 0, -452)),
              new BlockLocation("world", 6, -45, -453)),
          arguments(
              named("On overflow", new Vector(Integer.MAX_VALUE, 1, -1)),
              new BlockLocation("world", Integer.MIN_VALUE, -44, -2)),
          arguments(
              named("On underflow", new Vector(10, -50, Integer.MIN_VALUE)),
              new BlockLocation("world", 11, -95, Integer.MAX_VALUE)));
    }
  }
}
