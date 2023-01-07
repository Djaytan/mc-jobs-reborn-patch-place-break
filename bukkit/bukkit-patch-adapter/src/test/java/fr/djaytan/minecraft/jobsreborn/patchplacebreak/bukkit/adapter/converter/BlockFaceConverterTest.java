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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagVector;

class BlockFaceConverterTest {

  private BlockFaceConverter blockFaceConverter;

  @BeforeEach
  void setUp() {
    blockFaceConverter = new BlockFaceConverter();
  }

  @DisplayName("Conversion from a north direction")
  @Test
  void shouldReturnNorthDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.NORTH;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(0, 0, -1);
    assertEquals(expectedTagVector, tagVector);
  }

  @DisplayName("Conversion from a south-east direction")
  @Test
  void shouldReturnSouthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SOUTH_EAST;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(1, 0, 1);
    assertEquals(expectedTagVector, tagVector);
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnEastNorthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.EAST_NORTH_EAST;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(2, 0, -1);
    assertEquals(expectedTagVector, tagVector);
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnSelfDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SELF;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(0, 0, 0);
    assertEquals(expectedTagVector, tagVector);
  }
}
