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
package fr.djaytan.mc.jrppb.core;

import static org.assertj.core.api.Assertions.assertThat;

import fr.djaytan.mc.jrppb.api.entities.Block;
import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import fr.djaytan.mc.jrppb.api.properties.RestrictedBlocksProperties;
import fr.djaytan.mc.jrppb.api.properties.RestrictionMode;
import fr.djaytan.mc.jrppb.core.config.properties.RestrictedBlocksPropertiesImpl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BlocksFilterTest {

  private final RestrictedBlocksProperties restrictedBlocksProperties =
      new RestrictedBlocksPropertiesImpl(
          new HashSet<>(Arrays.asList("STONE", "DIRT")), RestrictionMode.BLACKLIST);
  private final BlocksFilter blocksFilter = new BlocksFilter(restrictedBlocksProperties);

  @Test
  void filterWithNominalValues() {
    // Given
    Block firstBlock = new Block(new BlockLocation("world", 0, 0, 0), "STONE");
    Block secondBlock = new Block(new BlockLocation("world", 1, 0, 0), "DIRT");
    Block thirdBlock = new Block(new BlockLocation("world", 0, 1, 0), "WOOD");
    Block fourthBlock = new Block(new BlockLocation("world", 0, 0, 1), "BEACON");
    Set<Block> blocks =
        new HashSet<>(Arrays.asList(firstBlock, secondBlock, thirdBlock, fourthBlock));

    // When
    Set<Block> actualValue = blocksFilter.filter(blocks);

    // Then
    assertThat(actualValue).isEqualTo(new HashSet<>(Arrays.asList(thirdBlock, fourthBlock)));
  }

  @Test
  void filterWithEmptyCollection() {
    // Given
    Set<Block> blocks = new HashSet<>();

    // When
    Set<Block> actualValue = blocksFilter.filter(blocks);

    // Then
    assertThat(actualValue).isEmpty();
  }
}
