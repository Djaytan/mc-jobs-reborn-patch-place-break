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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagVector;

@Singleton
public class PatchPlaceBreakImpl implements PatchPlaceBreakApi {

  private final Logger logger;
  private final TagPersistenceService tagPersistenceService;

  @Inject
  public PatchPlaceBreakImpl(@NotNull @Named("PatchPlaceBreakLogger") Logger logger,
      @NotNull TagPersistenceService tagPersistenceService) {
    this.logger = logger;
    this.tagPersistenceService = tagPersistenceService;
  }

  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> putTag(@NotNull TagLocation tagLocation,
      boolean isEphemeral) {
    Objects.requireNonNull(tagLocation);

    return CompletableFuture
        .runAsync(() -> tagPersistenceService.persistTag(isEphemeral, tagLocation).join());
  }

  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> putBackTagOnMovedBlocks(
      @NotNull List<TagLocation> tagLocations, @NotNull TagVector direction) {
    Objects.requireNonNull(tagLocations);
    Objects.requireNonNull(direction);

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
  public @NotNull CompletableFuture<Void> removeTag(@NotNull TagLocation tagLocation) {
    Objects.requireNonNull(tagLocation);

    return CompletableFuture.runAsync(() -> tagPersistenceService.removeTag(tagLocation).join());
  }

  public @NotNull CompletableFuture<Boolean> isPlaceAndBreakAction(
      @NotNull PatchActionType patchActionType, @NotNull TagLocation tagLocation) {
    Objects.requireNonNull(patchActionType);
    Objects.requireNonNull(tagLocation);

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

  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> verifyPatchApplication(
      @NotNull PatchActionType patchActionType, @NotNull TagLocation tagLocation,
      @NotNull String blockTypeName, boolean isEventCancelled, @NotNull String playerName,
      @NotNull String jobName, @NotNull List<String> detectedPotentialConflictingPluginsNames) {
    Objects.requireNonNull(patchActionType);
    Objects.requireNonNull(tagLocation);
    Objects.requireNonNull(blockTypeName);
    Objects.requireNonNull(playerName);
    Objects.requireNonNull(jobName);
    Objects.requireNonNull(detectedPotentialConflictingPluginsNames);

    return CompletableFuture.runAsync(() -> {
      boolean isPlaceAndBreakAction = isPlaceAndBreakAction(patchActionType, tagLocation).join();

      if (isPlaceAndBreakAction && !isEventCancelled) {
        logger.warning(() -> String.format(
            "Violation of a place-and-break patch detected! It's possible that's because of a"
                + " conflict with another plugin. Please, report this full log message to the"
                + " developer: player=%s, jobs=%s, actionType=%s, blockMaterial=%s,"
                + " detectedPotentialConflictingPlugins=%s",
            playerName, jobName, patchActionType.name(), blockTypeName,
            detectedPotentialConflictingPluginsNames));
      }
    });
  }

  private @NotNull CompletableFuture<Optional<Tag>> getTag(@NotNull TagLocation tagLocation) {
    return tagPersistenceService.findTagByLocation(tagLocation);
  }

  private boolean isActionToPatch(@NotNull PatchActionType patchActionType) {
    return Arrays.asList(PatchActionType.BREAK, PatchActionType.TNTBREAK, PatchActionType.PLACE)
        .contains(patchActionType);
  }
}
