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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

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

/**
 * This interface represents the API to apply place-and-break patch.
 *
 * <p>The patch of JobsReborn plugin works as follows: when an eligible event occurs
 * (e.g. a block place), a tag is attached to the block in order to cancel future jobs rewards
 * (payments, experience and points) when breaking this non-naturally placed block (e.g. preventing
 * diamond ores place-and-break exploit). This will be applied for {@link PatchActionType#BREAK} and
 * {@link PatchActionType#TNTBREAK} jobs actions.
 *
 * <p><i>Note: block placed by plugins aren't tagged too.</i>
 *
 * <p>It can work in the reverse order: when breaking a block, an "ephemeral" tag is placed to the
 * newly "placed" AIR block. It permits to cancel jobs rewards when placing another block in a
 * short-time delay (e.g. prevent sapling place-and-break exploit). This will be applied for
 * {@link PatchActionType#PLACE} jobs actions.
 *
 * <p>Nevertheless, an attached tag can be removed given specific conditions like when a block grow
 * event happens. This will permit farmers to achieve their job without seeing their action being
 * cancelled by this patch plugin. This is the purpose of the {@link #removeTag(TagLocation)}
 * method.
 *
 * <p>A tag can be placed with {@link #putTag(TagLocation, boolean)} method. The
 * {@link #putBackTagOnMovedBlocks(List, TagVector)} has a special purpose: to permit to put back tags
 * when blocks are moved (e.g. by block piston extend and retract events).
 *
 * <p>Finally, this controller give the possibility to check if the jobs ActionType involving a
 * given Block is a place-and-break exploit or no with the method
 * {@link #isPlaceAndBreakAction(PatchActionType, TagLocation)}.
 *
 * @see PatchActionType
 */
@Singleton
public class PatchPlaceBreakApi {

  private static final Duration EPHEMERAL_TAG_DURATION = Duration.ofSeconds(3);

  private final Logger logger;
  private final TagPersistenceService tagPersistenceService;

  @Inject
  public PatchPlaceBreakApi(@NotNull @Named("PatchPlaceBreakLogger") Logger logger,
      @NotNull TagPersistenceService tagPersistenceService) {
    this.logger = logger;
    this.tagPersistenceService = tagPersistenceService;
  }

  /**
   * This method permits to put a {@link Tag} on a given block's location.
   *
   * <p>If the tag must be considered as an "ephemeral" one, then a validity duration will be
   * specified to it. The duration is implementation specific, but a good default value could be
   * three seconds.
   *
   * @param tagLocation The block's location to be tagged.
   * @param isEphemeral <code>true</code> if the tag must be "ephemeral", <code>false</code>
   *                    otherwise.
   * @return void
   */
  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> putTag(@NotNull TagLocation tagLocation,
      boolean isEphemeral) {
    Objects.requireNonNull(tagLocation);

    return CompletableFuture
        .runAsync(() -> tagPersistenceService.persistTag(isEphemeral, tagLocation).join());
  }


  /**
   * This method permits to put back an existing {@link Tag} to a moved block,
   * which means that non-tagged block remains non-tagged because no {@link Tag}
   * are created by calling this method. The put back action shall be dispatched on next server
   * tick.
   *
   * <p><i>Note: specified blocks are expected to move one block only in the given direction.</i>
   *
   * @param tagLocations The initial locations of tags to be moved after the end of an event
   *                     processing.
   * @param direction The move direction of the specified blocks in order to reattach tags to these
   *                  new calculated locations.
   * @return void
   */
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


  /**
   * This method permits to remove a tag from a specified Block. This can be useful when the state
   * of the block change (e.g. crops like wheat).
   *
   * @param tagLocation The block's location which will have its tag to be removed if exists.
   * @return void
   */
  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> removeTag(@NotNull TagLocation tagLocation) {
    Objects.requireNonNull(tagLocation);

    return CompletableFuture.runAsync(() -> tagPersistenceService.removeTag(tagLocation).join());
  }

  /**
   * This method permit to check if a jobs ActionType with a given Block is a place-and-break
   * exploit or no.
   *
   * <p>This method will return <code>true</code> if all theses following criteria are meet:
   *
   * <ul>
   *   <li>The ActionType is eligible (the list of ActionType eligible is implementation specific,
   *       but we expect the list to contain at least {@link PatchActionType#BREAK},
   *       {@link PatchActionType#TNTBREAK} and {@link PatchActionType#PLACE}) ;
   *   <li>A "non-ephemeral" tag is attached to the given block or the "ephemeral" one found hasn't
   *       expired.
   * </ul>
   *
   * @param patchActionType The jobs ActionType.
   * @param tagLocation The block's location with a potential place-and-break patch tag.
   * @return <code>true</code> if it's a place-and-break exploit, <code>false</code> otherwise.
   */
  // TODO: rename it
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

  // TODO: create a separated class
  /**
   * The purpose of this method is to verify the well-application of the patch if required for a
   * given jobs action. If a place-and-break action has been detected (via a call to {@link
   * #isPlaceAndBreakAction(PatchActionType, TagLocation)}) but the event still not cancelled then a warning
   * log will be sent.
   *
   * @param patchActionType The jobs ActionType.
   * @param tagLocation The tag location.
   * @param blockTypeName The block with a potential place-and-break patch tag.
   * @param isEventCancelled <code>true</code> if the event is cancelled, <code>false</code>
   *                         otherwise.
   * @param playerName The involved player by the patch.
   * @param jobName The involved job by the patch.
   * @param detectedPotentialConflictingPluginsNames The list of event handlers which could be the
   *                                                 source of the issue if it exists.
   * @return void
   */
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
      if (isPlaceAndBreakAction(patchActionType, tagLocation).join() && !isEventCancelled) {
        logger.warning(String.format(
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
