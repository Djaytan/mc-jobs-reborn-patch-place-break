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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagVector;
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

  @Test
  @DisplayName("Nominal adjustment case")
  void shouldAdjustSuccessfullyOnNominalCase() {
    // Given
    double modX = 5.4D;
    double modY = 0.0D;
    double modZ = -452.568D;
    TagVector tagVector = TagVector.of(modX, modY, modZ);

    String worldName = "world";
    double initX = 485356.0D;
    double initY = -42.485784187415D;
    double initZ = 1.214D;
    TagLocation tagLocation = TagLocation.of(worldName, initX, initY, initZ);

    // When
    TagLocation adjustedTagLocation = tagLocation.adjust(tagVector);

    // Then
    String expectedWorld = "world";
    double expectedX = 485361.4D;
    double expectedY = -42.485784187415D;
    double expectedZ = -451.354D;
    TagLocation expectedTagLocation =
        TagLocation.of(expectedWorld, expectedX, expectedY, expectedZ);

    assertAll(() -> assertThat(adjustedTagLocation).isEqualTo(expectedTagLocation),
        () -> assertThat(adjustedTagLocation).isNotSameAs(tagLocation));
  }

  @Test
  @DisplayName("Number overflow adjustment case")
  void shouldAdjustSuccessfullyOnNumberOverflow() {
    // Given
    double modX = Double.MAX_VALUE;
    double modY = 15.256D;
    double modZ = 0.0D;
    TagVector tagVector = TagVector.of(modX, modY, modZ);

    String worldName = "world";
    double initX = 10000000.0D;
    double initY = -45.5263D;
    double initZ = 3.14D;
    TagLocation tagLocation = TagLocation.of(worldName, initX, initY, initZ);

    // When
    TagLocation adjustedTagLocation = tagLocation.adjust(tagVector);

    // Then
    String expectedWorld = "world";
    double expectedX = Double.MAX_VALUE;
    double expectedY = -30.2703D;
    double expectedZ = 3.14D;
    TagLocation expectedTagLocation =
        TagLocation.of(expectedWorld, expectedX, expectedY, expectedZ);

    assertAll(() -> assertThat(adjustedTagLocation).isEqualTo(expectedTagLocation),
        () -> assertThat(adjustedTagLocation).isNotSameAs(tagLocation));
  }

  @Test
  @DisplayName("Number underflow adjustment case")
  void shouldAdjustSuccessfullyOnNumberUnderflow() {
    // Given
    double modX = 452.2D;
    double modY = Double.MIN_VALUE;
    double modZ = 0.0D;
    TagVector tagVector = TagVector.of(modX, modY, modZ);

    String worldName = "world";
    double initX = 10.0D;
    double initY = -45.5263D;
    double initZ = 3.14D;
    TagLocation tagLocation = TagLocation.of(worldName, initX, initY, initZ);

    // When
    TagLocation adjustedTagLocation = tagLocation.adjust(tagVector);

    // Then
    String expectedWorld = "world";
    double expectedX = 462.2D;
    double expectedY = -45.5263D;
    double expectedZ = 3.14D;
    TagLocation expectedTagLocation =
        TagLocation.of(expectedWorld, expectedX, expectedY, expectedZ);

    assertAll(() -> assertThat(adjustedTagLocation).isEqualTo(expectedTagLocation),
        () -> assertThat(adjustedTagLocation).isNotSameAs(tagLocation));
  }
}
