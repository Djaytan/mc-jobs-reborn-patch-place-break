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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Block;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictedBlocksProperties;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the blocks' filter based on the corresponding properties.
 *
 * @see RestrictedBlocksProperties
 */
@Singleton
public final class BlocksFilter {

  private final RestrictedBlocksProperties restrictedBlocksProperties;

  @Inject
  public BlocksFilter(@NotNull RestrictedBlocksProperties restrictedBlocksProperties) {
    this.restrictedBlocksProperties = restrictedBlocksProperties;
  }

  /**
   * Filters the given blocks based on their material by removing any blocks having ones marked as
   * restricted. Filters the given blocks based on their material depending on the current {@link
   * fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictionMode} which apply.
   *
   * @param blocks The blocks to filter.
   * @return The filtered blocks according the currently applicable restriction mode.
   */
  public @NotNull Set<Block> filter(@NotNull Collection<Block> blocks) {
    return blocks.stream()
        .filter(block -> !restrictedBlocksProperties.isRestricted(block.material()))
        .collect(Collectors.toSet());
  }
}
