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

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Block;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictedBlocksProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPair;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPairSet;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.TagRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/** Implementation of {@link PatchPlaceBreakApi}. */
@Singleton
@Slf4j
public class PatchPlaceBreakImpl implements PatchPlaceBreakApi {

  private final BlocksFilter blocksFilter;
  private final Clock clock;
  private final RestrictedBlocksProperties restrictedBlocksProperties;
  private final TagRepository tagRepository;

  @Inject
  public PatchPlaceBreakImpl(
      @NotNull BlocksFilter blocksFilter,
      @NotNull Clock clock,
      @NotNull RestrictedBlocksProperties restrictedBlocksProperties,
      @NotNull TagRepository tagRepository) {
    this.blocksFilter = blocksFilter;
    this.clock = clock;
    this.restrictedBlocksProperties = restrictedBlocksProperties;
    this.tagRepository = tagRepository;
  }

  public @NotNull CompletableFuture<Void> putTag(@NotNull Block block, boolean isEphemeral) {
    return CompletableFuture.runAsync(
        () -> {
          if (restrictedBlocksProperties.isRestricted(block.getMaterial())) {
            return;
          }

          LocalDateTime localDateTime = LocalDateTime.now(clock);
          Tag tag = new Tag(block.getBlockLocation(), isEphemeral, localDateTime);
          tagRepository.put(tag);
        });
  }

  public @NotNull CompletableFuture<Void> moveTags(
      @NotNull Set<Block> blocks, @NotNull Vector direction) {
    return CompletableFuture.runAsync(
        () -> {
          Set<Block> filteredBlocks = blocksFilter.filter(blocks);

          if (filteredBlocks.isEmpty()) {
            return;
          }

          OldNewBlockLocationPairSet oldNewLocationPairs =
              new OldNewBlockLocationPairSet(
                  filteredBlocks.stream()
                      .map(
                          oldBlock ->
                              new OldNewBlockLocationPair(
                                  oldBlock.getBlockLocation(),
                                  BlockLocation.from(oldBlock.getBlockLocation(), direction)))
                      .collect(Collectors.toSet()));

          tagRepository.updateLocations(oldNewLocationPairs);
        });
  }

  public @NotNull CompletableFuture<Void> removeTag(@NotNull Block block) {
    return CompletableFuture.runAsync(
        () -> {
          if (restrictedBlocksProperties.isRestricted(block.getMaterial())) {
            return;
          }

          tagRepository.delete(block.getBlockLocation());
        });
  }

  public boolean isPlaceAndBreakExploit(
      @NotNull BlockActionType blockActionType, @NotNull Block block) {
    if (restrictedBlocksProperties.isRestricted(block.getMaterial())) {
      return false;
    }

    Optional<Tag> tag = tagRepository.findByLocation(block.getBlockLocation());

    if (!tag.isPresent()) {
      return false;
    }

    if (!tag.get().isEphemeral()) {
      return true;
    }

    LocalDateTime localDateTime = LocalDateTime.now(clock);
    Duration timeElapsed = Duration.between(tag.get().getInitLocalDateTime(), localDateTime);

    return timeElapsed.minus(EPHEMERAL_TAG_DURATION).isNegative();
  }
}
