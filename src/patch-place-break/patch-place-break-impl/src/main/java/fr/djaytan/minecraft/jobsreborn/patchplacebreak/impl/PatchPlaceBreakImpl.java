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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagVector;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of {@link PatchPlaceBreakApi}.
 */
@Singleton
@Slf4j
public class PatchPlaceBreakImpl implements PatchPlaceBreakApi {

  private final TagRepository tagRepository;

  @Inject
  public PatchPlaceBreakImpl(@NonNull TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public void putTag(@NonNull TagLocation tagLocation, boolean isEphemeral) {
    CompletableFuture.runAsync(() -> {
      UUID tagUuid = UUID.randomUUID();
      LocalDateTime localDateTime = LocalDateTime.now();
      Tag tag = Tag.of(tagUuid, localDateTime, isEphemeral, tagLocation);
      tagRepository.put(tag);
    });
  }

  public void moveTags(@NonNull Collection<TagLocation> tagLocations,
      @NonNull TagVector direction) {
    CompletableFuture.runAsync(() -> {
      for (TagLocation oldTagLocation : tagLocations) {
        Optional<Tag> tag = tagRepository.findByLocation(oldTagLocation);

        if (!tag.isPresent()) {
          continue;
        }

        TagLocation newTagLocation = TagLocation.fromMove(oldTagLocation, direction);

        moveTag(oldTagLocation, newTagLocation, tag.get().isEphemeral());
      }
    });
  }

  private void moveTag(@NonNull TagLocation oldTagLocation, @NonNull TagLocation newTagLocation,
      boolean isEphemeral) {
    putTag(newTagLocation, isEphemeral);
    // TODO: old tag must be removed, but it must be done with a transaction
  }

  public void removeTags(@NonNull TagLocation tagLocation) {
    CompletableFuture.runAsync(() -> tagRepository.delete(tagLocation));
  }

  public @NonNull CompletableFuture<Boolean> isPlaceAndBreakExploit(
      @NonNull BlockActionType blockActionType, @NonNull TagLocation tagLocation) {
    return CompletableFuture.supplyAsync(() -> {
      Optional<Tag> tag = tagRepository.findByLocation(tagLocation);

      if (!tag.isPresent()) {
        return false;
      }

      if (!tag.get().isEphemeral()) {
        return true;
      }

      LocalDateTime localDateTime = LocalDateTime.now();
      Duration timeElapsed = Duration.between(tag.get().getInitLocalDateTime(), localDateTime);

      return timeElapsed.minus(EPHEMERAL_TAG_DURATION).isNegative();
    });
  }
}
