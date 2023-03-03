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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import static org.assertj.core.api.Assertions.assertThat;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlockFaceConverterTest {

  private BlockFaceConverter blockFaceConverter;

  @BeforeEach
  void beforeEach() {
    blockFaceConverter = new BlockFaceConverter();
  }

  @Test
  @DisplayName("Conversion from a north direction")
  void shouldReturnNorthDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.NORTH;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(0, 0, -1));
  }

  @Test
  @DisplayName("Conversion from a south-east direction")
  void shouldReturnSouthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SOUTH_EAST;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(1, 0, 1));
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnEastNorthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.EAST_NORTH_EAST;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(2, 0, -1));
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnSelfDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SELF;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(0, 0, 0));
  }
}
