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
package fr.djaytan.mc.jrppb.paper.adapter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import fr.djaytan.mc.jrppb.api.entities.Vector;
import java.util.stream.Stream;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BlockFaceConverterTest {

  private BlockFaceConverter blockFaceConverter;

  @BeforeEach
  void setUp() {
    blockFaceConverter = new BlockFaceConverter();
  }

  /**
   * To better understand naming convention: <a
   * href="https://en.wikipedia.org/wiki/Cardinal_direction">Cardinal direction - Wikipedia</a>.
   *
   * <ul>
   *   <li>Cardinal points = NORTH, SOUTH, EAST, WEST
   *   <li>Primary inter-cardinal points = NORTH_EAST, NORTH_WEST, SOUTH_WEST, SOUTH_WEST
   *   <li>Secondary inter-cardinal points = WEST_NORTH_WEST, EAST_NORTH_EAST, ...
   * </ul>
   *
   * However, since we are in a 3D, we refer to cartesian space concept instead but only for
   * cardinal points.
   */
  @Nested
  class WhenConverting {

    @ParameterizedTest
    @MethodSource
    void withCartesianBlockFace_shouldReturnTheEquivalentVector(
        @NotNull BlockFace blockFace, @NotNull Vector expectedVector) {
      assertThat(blockFaceConverter.convert(blockFace)).isEqualTo(expectedVector);
    }

    private static @NotNull Stream<Arguments>
        withCartesianBlockFace_shouldReturnTheEquivalentVector() {
      return Stream.of(
          arguments(BlockFace.EAST, new Vector(1, 0, 0)),
          arguments(BlockFace.WEST, new Vector(-1, 0, 0)),
          arguments(BlockFace.UP, new Vector(0, 1, 0)),
          arguments(BlockFace.DOWN, new Vector(0, -1, 0)),
          arguments(BlockFace.SOUTH, new Vector(0, 0, 1)),
          arguments(BlockFace.NORTH, new Vector(0, 0, -1)));
    }

    @ParameterizedTest
    @MethodSource
    void withPrimaryIntercardinalBlockFace_shouldReturnTheEquivalentVector(
        @NotNull BlockFace blockFace, @NotNull Vector expectedVector) {
      assertThat(blockFaceConverter.convert(blockFace)).isEqualTo(expectedVector);
    }

    private static @NotNull Stream<Arguments>
        withPrimaryIntercardinalBlockFace_shouldReturnTheEquivalentVector() {
      return Stream.of(
          arguments(BlockFace.NORTH_EAST, new Vector(1, 0, -1)),
          arguments(BlockFace.NORTH_WEST, new Vector(-1, 0, -1)),
          arguments(BlockFace.SOUTH_EAST, new Vector(1, 0, 1)),
          arguments(BlockFace.SOUTH_WEST, new Vector(-1, 0, 1)));
    }

    @ParameterizedTest
    @MethodSource
    void withSecondaryIntercardinalBlockFace_shouldReturnTheEquivalentVector(
        @NotNull BlockFace blockFace, @NotNull Vector expectedVector) {
      assertThat(blockFaceConverter.convert(blockFace)).isEqualTo(expectedVector);
    }

    private static @NotNull Stream<Arguments>
        withSecondaryIntercardinalBlockFace_shouldReturnTheEquivalentVector() {
      return Stream.of(
          arguments(BlockFace.NORTH_NORTH_EAST, new Vector(1, 0, -2)),
          arguments(BlockFace.NORTH_NORTH_WEST, new Vector(-1, 0, -2)),
          arguments(BlockFace.SOUTH_SOUTH_EAST, new Vector(1, 0, 2)),
          arguments(BlockFace.SOUTH_SOUTH_WEST, new Vector(-1, 0, 2)),
          arguments(BlockFace.EAST_NORTH_EAST, new Vector(2, 0, -1)),
          arguments(BlockFace.WEST_NORTH_WEST, new Vector(-2, 0, -1)),
          arguments(BlockFace.EAST_SOUTH_EAST, new Vector(2, 0, 1)),
          arguments(BlockFace.WEST_SOUTH_WEST, new Vector(-2, 0, 1)));
    }

    @Test
    void withSelfOrientedBlockFace_shouldReturnTheEquivalentVector() {
      assertThat(blockFaceConverter.convert(BlockFace.SELF)).isEqualTo(new Vector(0, 0, 0));
    }
  }
}
