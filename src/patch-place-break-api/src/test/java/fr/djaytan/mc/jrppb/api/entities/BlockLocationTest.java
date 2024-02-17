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

    /**
     * For now, the tests make in evidence a limitation of the current implementation since we can't
     * scale indefinitely in cartesian coordinates as designed initially in Minecraft. In practice,
     * there is no real impact since limitations will start to occur when reaching very high
     * coordinates near to {@link Integer#MAX_VALUE}. Tho, it remains cleaner to fix that (maybe
     * thanks to {@link java.math.BigInteger}?). An interesting problem to solve, in fact.
     */
    @Nested
    class WithCopyConstructor {

      @Test
      void andNominalValues_shouldMatchExpectedValues() {
        assertThat(BlockLocation.from(BLOCK_LOCATION, new Vector(5, 0, -452)))
            .isEqualTo(new BlockLocation("world", -49, 67, 4420));
      }

      @Test
      void onOverflow_shouldMatchExpectedValues() {
        assertThat(BlockLocation.from(BLOCK_LOCATION, new Vector(Integer.MAX_VALUE, 1, -1)))
            .isEqualTo(new BlockLocation("world", Integer.MAX_VALUE - Math.abs(X), 68, 4871));
      }

      @Test
      void onUnderflow_shouldMatchExpectedValues() {
        assertThat(BlockLocation.from(BLOCK_LOCATION, new Vector(10, -50, Integer.MIN_VALUE)))
            .isEqualTo(new BlockLocation("world", -44, 17, Integer.MIN_VALUE + Math.abs(Z)));
      }
    }
  }
}
