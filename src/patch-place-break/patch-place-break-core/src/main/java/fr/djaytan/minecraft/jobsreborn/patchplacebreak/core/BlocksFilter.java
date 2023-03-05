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
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public final class BlocksFilter {

  /**
   * Represents a filter for a collection of {@link Block} objects based on their materials.
   */
  private final RestrictedBlocksProperties restrictedBlocksProperties;

  @Inject
  public BlocksFilter(RestrictedBlocksProperties restrictedBlocksProperties) {
    this.restrictedBlocksProperties = restrictedBlocksProperties;
  }

  /**
   * Filters the given {@link Collection} of {@link Block} objects based on their materials, removing any
   * blocks with materials that are marked as restricted by the {@link RestrictedBlocksProperties} instance.
   * @param  blocks the {@link Collection} of {@link Block} objects to filter
   * @return        a new {@link Set} containing the filtered {@link Block} objects without any restricted materials
   */
  public @NonNull Set<Block> filter(@NonNull Collection<Block> blocks) {
    return blocks.stream()
        .filter(block -> !restrictedBlocksProperties.isRestricted(block.getMaterial()))
        .collect(Collectors.toSet());
  }
}
