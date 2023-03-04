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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OldNewBlockLocationPairTest {

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("With nominal values")
    void withNominalValues_shouldSuccess() {
      // Given
      BlockLocation oldBlockLocation = BlockLocation.of("world", 12, 64, -35);
      BlockLocation newBlockLocation = BlockLocation.of("world", 12, 64, -36);

      // When
      OldNewBlockLocationPair oldNewBlockLocationPair =
          new OldNewBlockLocationPair(oldBlockLocation, newBlockLocation);

      // Then
      assertAll(
          () ->
              assertThat(oldNewBlockLocationPair.getOldBlockLocation()).isEqualTo(oldBlockLocation),
          () ->
              assertThat(oldNewBlockLocationPair.getNewBlockLocation())
                  .isEqualTo(newBlockLocation));
    }

    @Test
    @DisplayName("With same locations")
    void withSameLocations_shouldThrowException() {
      // Given
      BlockLocation oldBlockLocation = BlockLocation.of("world", 12, 64, -35);
      BlockLocation newBlockLocation = BlockLocation.of("world", 12, 64, -35);

      // When
      ThrowingCallable throwingCallable =
          () -> new OldNewBlockLocationPair(oldBlockLocation, newBlockLocation);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(IllegalStateException.class);
    }
  }

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashCode_shouldMetContracts() {
    EqualsVerifier.forClass(OldNewBlockLocationPair.class).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(OldNewBlockLocationPair.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
