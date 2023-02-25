/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import java.util.stream.Stream;
import lombok.NonNull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BlockLocationTest {

  @Nested
  @DisplayName("When instantiating")
  @TestInstance(Lifecycle.PER_CLASS)
  class WhenInstantiating {

    @Test
    @DisplayName("With all args constructor and nominal values")
    void withAllArgsConstructorAndNominalValues_shouldSuccess() {
      // Given
      String worldName = "world";
      int x = -54;
      int y = 67;
      int z = 4872;

      // When
      BlockLocation blockLocation = BlockLocation.of(worldName, x, y, z);

      // Then
      assertAll(
          "Verification of returned values from getters",
          () -> assertThat(blockLocation.getWorldName()).isEqualTo(worldName),
          () -> assertThat(blockLocation.getX()).isEqualTo(x),
          () -> assertThat(blockLocation.getY()).isEqualTo(y),
          () -> assertThat(blockLocation.getZ()).isEqualTo(z));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With copy constructor")
    void withCopyConstructor_shouldCreateNewInstanceMatchingExpectedValue(
        @NonNull Vector givenDirection, @NonNull BlockLocation expectedValue) {
      // Given
      String worldName = "world";
      int initX = 1;
      int initY = -45;
      int initZ = -1;
      BlockLocation blockLocation = BlockLocation.of(worldName, initX, initY, initZ);

      // When
      BlockLocation movedBlockLocation = BlockLocation.from(blockLocation, givenDirection);

      // Then
      assertAll(
          () -> assertThat(movedBlockLocation).isNotSameAs(blockLocation),
          () -> assertThat(movedBlockLocation).isEqualTo(expectedValue));
    }

    private @NonNull Stream<Arguments>
        withCopyConstructor_shouldCreateNewInstanceMatchingExpectedValue() {
      return Stream.of(
          Arguments.of(
              Named.of("With nominal values", Vector.of(5, 0, -452)),
              BlockLocation.of("world", 6, -45, -453)),
          Arguments.of(
              Named.of("On overflow", Vector.of(Integer.MAX_VALUE, 1, -1)),
              BlockLocation.of("world", Integer.MIN_VALUE, -44, -2)),
          Arguments.of(
              Named.of("On underflow", Vector.of(10, -50, Integer.MIN_VALUE)),
              BlockLocation.of("world", 11, -95, Integer.MAX_VALUE)));
    }
  }

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashCode_shouldMetContracts() {
    EqualsVerifier.forClass(BlockLocation.class).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(BlockLocation.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
