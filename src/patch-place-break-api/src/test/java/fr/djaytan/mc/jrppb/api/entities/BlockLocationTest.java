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
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BlockLocationTest {

  private static final String WORLD_NAME = "world";
  private static final int X = -54;
  private static final int Y = 67;
  private static final int Z = 4872;
  private static final BlockLocation BLOCK_LOCATION = new BlockLocation(WORLD_NAME, X, Y, Z);

  @Nested
  class WhenInstantiating {

    @Test
    void withAllArgsConstructorAndNominalValues_shouldMatchExpectedValues() {
      BlockLocation blockLocation = new BlockLocation(WORLD_NAME, X, Y, Z);

      assertAll(
          () -> assertThat(blockLocation.worldName()).isEqualTo(WORLD_NAME),
          () -> assertThat(blockLocation.x()).isEqualTo(X),
          () -> assertThat(blockLocation.y()).isEqualTo(Y),
          () -> assertThat(blockLocation.z()).isEqualTo(Z));
    }

    @Nested
    class WithCopyConstructor {

      @Test
      void andNominalValues_shouldMatchExpectedValues() {
        // Given
        Vector vector = new Vector(5, 0, -452);

        // When
        BlockLocation movedBlockLocation = BlockLocation.from(BLOCK_LOCATION, vector);

        // Then
        assertThat(movedBlockLocation).isEqualTo(new BlockLocation("world", -49, 67, 4420));
      }

      @Test
      void onOverflow_shouldMatchExpectedValues() {
        // Given
        Vector vector = new Vector(0, 0, Integer.MAX_VALUE);

        // When
        Exception exception = catchException(() -> BlockLocation.from(BLOCK_LOCATION, vector));

        // Then
        assertThat(exception)
            .isExactlyInstanceOf(ArithmeticException.class)
            .hasMessage("integer overflow");
      }

      @Test
      void onUnderflow_shouldMatchExpectedValues() {
        // Given
        Vector vector = new Vector(Integer.MIN_VALUE, 0, 0);

        // When
        Exception exception = catchException(() -> BlockLocation.from(BLOCK_LOCATION, vector));

        // Then
        assertThat(exception)
            .isExactlyInstanceOf(ArithmeticException.class)
            .hasMessage("integer overflow");
      }
    }
  }
}
