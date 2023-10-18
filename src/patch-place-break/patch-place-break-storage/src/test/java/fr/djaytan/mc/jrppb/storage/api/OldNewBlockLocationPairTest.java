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
package fr.djaytan.mc.jrppb.storage.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertAll;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
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
      BlockLocation oldBlockLocation = new BlockLocation("world", 12, 64, -35);
      BlockLocation newBlockLocation = new BlockLocation("world", 12, 64, -36);

      // When
      OldNewBlockLocationPair oldNewBlockLocationPair =
          new OldNewBlockLocationPair(oldBlockLocation, newBlockLocation);

      // Then
      assertAll(
          () -> assertThat(oldNewBlockLocationPair.oldBlockLocation()).isEqualTo(oldBlockLocation),
          () -> assertThat(oldNewBlockLocationPair.newBlockLocation()).isEqualTo(newBlockLocation));
    }

    @Test
    @DisplayName("With same locations")
    void withSameLocations_shouldThrowException() {
      // Given
      BlockLocation oldBlockLocation = new BlockLocation("world", 12, 64, -35);
      BlockLocation newBlockLocation = new BlockLocation("world", 12, 64, -35);

      // When
      Exception exception =
          catchException(() -> new OldNewBlockLocationPair(oldBlockLocation, newBlockLocation));

      // Then
      assertThat(exception).isExactlyInstanceOf(IllegalStateException.class);
    }
  }
}
