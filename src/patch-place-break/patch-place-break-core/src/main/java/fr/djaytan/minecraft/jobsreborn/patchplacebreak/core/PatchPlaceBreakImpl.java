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
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.*;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPair;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPairSet;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.TagRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/** Implementation of {@link PatchPlaceBreakApi}. */
@Singleton
@Slf4j
public class PatchPlaceBreakImpl implements PatchPlaceBreakApi {

  private final TagRepository tagRepository;

  @Inject
  public PatchPlaceBreakImpl(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public @NonNull CompletableFuture<Void> putTag(
      @NonNull Block block, boolean isEphemeral) {
    return CompletableFuture.runAsync(
        () -> {
          LocalDateTime localDateTime = LocalDateTime.now();
          Tag tag = Tag.of(block.getBlockLocation(), isEphemeral, localDateTime);
          tagRepository.put(tag);
        });
  }

  public @NonNull CompletableFuture<Void> moveTags(
      @NonNull Set<Block> blocks, @NonNull Vector direction) {
    return CompletableFuture.runAsync(
        () -> {
          OldNewBlockLocationPairSet oldNewLocationPairs =
              new OldNewBlockLocationPairSet(
                  blockLocations.stream()
                      .map(
                          oldBlockLocation ->
                              new OldNewBlockLocationPair(
                                  oldBlockLocation.getBlockLocation(),
                                  BlockLocation.from(
                                      oldBlockLocation.getBlockLocation(), direction)))
                      .collect(Collectors.toSet()));

          tagRepository.updateLocations(oldNewLocationPairs);
        });
  }

  public @NonNull CompletableFuture<Void> removeTag(@NonNull Block block) {
    return CompletableFuture.runAsync(() -> tagRepository.delete(block));
  }

  public boolean isPlaceAndBreakExploit(
      @NonNull BlockActionType blockActionType, @NonNull Block block) {
    Optional<Tag> tag = tagRepository.findByLocation(block.getBlockLocation());

    if (!tag.isPresent()) {
      return false;
    }

    if (!tag.get().isEphemeral()) {
      return true;
    }

    LocalDateTime localDateTime = LocalDateTime.now();
    Duration timeElapsed = Duration.between(tag.get().getInitLocalDateTime(), localDateTime);

    return timeElapsed.minus(EPHEMERAL_TAG_DURATION).isNegative();
  }
}
