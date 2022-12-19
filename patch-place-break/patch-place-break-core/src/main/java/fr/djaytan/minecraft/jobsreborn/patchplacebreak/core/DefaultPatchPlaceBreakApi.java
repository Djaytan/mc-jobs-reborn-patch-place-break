/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.PatchActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagVector;
import lombok.NonNull;

@Singleton
public class DefaultPatchPlaceBreakApi implements PatchPlaceBreakApi {

  private final TagRepository tagRepository;

  @Inject
  public DefaultPatchPlaceBreakApi(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @CanIgnoreReturnValue
  public @NonNull CompletableFuture<Void> putTag(@NonNull TagLocation tagLocation,
      boolean isEphemeral) {
    return CompletableFuture.runAsync(() -> tagRepository.put(isEphemeral, tagLocation).join());
  }

  @CanIgnoreReturnValue
  public @NonNull CompletableFuture<Void> putBackTagOnMovedBlocks(
      @NonNull Collection<TagLocation> tagLocations, @NonNull TagVector direction) {
    return CompletableFuture.runAsync(() -> {
      for (TagLocation tagLocation : tagLocations) {
        Optional<Tag> tag = getTag(tagLocation).join();

        if (!tag.isPresent()) {
          continue;
        }

        TagLocation newTagLocation = tagLocation.adjust(direction);
        putTag(newTagLocation, false).join();
      }
    });
  }

  @CanIgnoreReturnValue
  public @NonNull CompletableFuture<Void> removeTag(@NonNull TagLocation tagLocation) {
    return CompletableFuture.runAsync(() -> tagRepository.delete(tagLocation).join());
  }

  public @NonNull CompletableFuture<Boolean> isPlaceAndBreakAction(
      @NonNull PatchActionType patchActionType, @NonNull TagLocation tagLocation) {
    return CompletableFuture.supplyAsync(() -> {
      Optional<Tag> tag = getTag(tagLocation).join();

      if (!isActionToPatch(patchActionType) || !tag.isPresent()) {
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

  private @NonNull CompletableFuture<Optional<Tag>> getTag(@NonNull TagLocation tagLocation) {
    return tagRepository.findByLocation(tagLocation);
  }

  private boolean isActionToPatch(@NonNull PatchActionType patchActionType) {
    return Arrays.asList(PatchActionType.BREAK, PatchActionType.TNTBREAK, PatchActionType.PLACE)
        .contains(patchActionType);
  }
}
