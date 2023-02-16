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

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagVector;
import lombok.NonNull;
import nl.jqno.equalsverifier.EqualsVerifier;

class TagLocationTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    String worldName = "world";
    double x = -54.0D;
    double y = 67.785D;
    double z = 4872.45152D;

    // When
    TagLocation tagLocation = TagLocation.of(worldName, x, y, z);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertThat(tagLocation.getWorldName()).isEqualTo(worldName),
        () -> assertThat(tagLocation.getX()).isEqualTo(x),
        () -> assertThat(tagLocation.getY()).isEqualTo(y),
        () -> assertThat(tagLocation.getZ()).isEqualTo(z));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void shouldEqualsAndHashcodeWellImplemented() {
    EqualsVerifier.forClass(TagLocation.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void shouldToStringWellImplemented() {
    ToStringVerifier.forClass(TagLocation.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @ParameterizedTest(name = "{index} - {0}")
  @MethodSource
  void whenMoving_shouldCreateNewInstanceMatchingExpectedValue(@NonNull TagVector givenDirection,
      @NonNull TagLocation expectedValue) {
    // Given
    String worldName = "world";
    double initX = 1000;
    double initY = -45;
    double initZ = 1.5;
    TagLocation tagLocation = TagLocation.of(worldName, initX, initY, initZ);

    // When
    TagLocation movedTagLocation = tagLocation.move(givenDirection);

    // Then
    assertAll(() -> assertThat(movedTagLocation).isNotSameAs(tagLocation),
        () -> assertThat(movedTagLocation).isEqualTo(expectedValue));
  }

  private static @NonNull Stream<Arguments> whenMoving_shouldCreateNewInstanceMatchingExpectedValue() {
    return Stream.of(
        Arguments.of(Named.of("With nominal values", TagVector.of(5.4, 0, -452.56)),
            TagLocation.of("world", 1005.4, -45, -451.06)),
        Arguments.of(Named.of("On overflow", TagVector.of(Double.MAX_VALUE, 1, -1)),
            TagLocation.of("world", Double.MAX_VALUE, -44, 0.5)),
        Arguments.of(Named.of("On underflow", TagVector.of(10, -50, Double.MIN_VALUE)),
            TagLocation.of("world", 1010, -95, 1.5)));
  }
}
