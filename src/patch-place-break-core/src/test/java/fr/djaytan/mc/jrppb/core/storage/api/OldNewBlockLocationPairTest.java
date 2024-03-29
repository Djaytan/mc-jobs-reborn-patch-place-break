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
package fr.djaytan.mc.jrppb.core.storage.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OldNewBlockLocationPairTest {

  @Nested
  class WhenInstantiating {

    private static final BlockLocation BLOCK_LOCATION_1 = new BlockLocation("world", -35, 8, 62);
    private static final BlockLocation BLOCK_LOCATION_2 = new BlockLocation("world", -35, 8, 63);

    @Test
    void withNominalValues_shouldSuccess() {
      OldNewBlockLocationPair oldNewBlockLocationPair =
          new OldNewBlockLocationPair(BLOCK_LOCATION_1, BLOCK_LOCATION_2);

      assertAll(
          () -> assertThat(oldNewBlockLocationPair.oldBlockLocation()).isEqualTo(BLOCK_LOCATION_1),
          () -> assertThat(oldNewBlockLocationPair.newBlockLocation()).isEqualTo(BLOCK_LOCATION_2));
    }

    @Test
    void withSameLocations_shouldThrowException() {
      assertThatThrownBy(() -> new OldNewBlockLocationPair(BLOCK_LOCATION_1, BLOCK_LOCATION_1))
          .isExactlyInstanceOf(IllegalStateException.class);
    }
  }
}
