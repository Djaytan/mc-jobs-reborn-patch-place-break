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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import java.util.stream.Stream;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(SoftAssertionsExtension.class)
class BlockLocationTest {

  @InjectSoftAssertions private SoftAssertions softly;

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
      BlockLocation blockLocation = new BlockLocation(worldName, x, y, z);

      // Then
      softly.assertThat(blockLocation.getWorldName()).isEqualTo(worldName);
      softly.assertThat(blockLocation.getX()).isEqualTo(x);
      softly.assertThat(blockLocation.getY()).isEqualTo(y);
      softly.assertThat(blockLocation.getZ()).isEqualTo(z);
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With copy constructor")
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
      softly.assertThat(movedBlockLocation).isNotSameAs(blockLocation);
      softly.assertThat(movedBlockLocation).isEqualTo(expectedValue);
    }

    private @NotNull Stream<Arguments>
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
