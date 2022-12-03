/*
 * JobsReborn extension to patch place-break (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.djaytan.minecraft.jobs_reborn_patch_place_break;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.PatchPlaceAndBreakService;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.PatchPlaceAndBreakTag;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.utils.LocationConverter;

/**
 * This interface represents the API to apply place-and-break patch.
 *
 * <p>The patch of JobsReborn plugin works as follows: when an eligible event occurs (e.g. {@link
 * org.bukkit.event.block.BlockPlaceEvent}), a tag is attached to the block in order to cancel
 * future jobs rewards (payments, experience and points) when breaking this non-naturally placed
 * block (e.g. preventing diamond ores place-and-break exploit). This will be applied for {@link
 * ActionType#BREAK} and {@link ActionType#TNTBREAK} jobs actions.
 *
 * <p><i>Note: block placed by plugins aren't tagged too.</i>
 *
 * <p>It can work in the reverse order: when breaking a block, an "ephemeral" tag is placed to the
 * newly "placed" {@link org.bukkit.Material#AIR} block. It permits to cancel jobs rewards when
 * placing another block in a short-time delay (e.g. prevent sapling place-and-break exploit). This
 * will be applied for {@link ActionType#PLACE} jobs actions.
 *
 * <p>Nevertheless, an attached tag can be removed given specific conditions like when an {@link
 * org.bukkit.event.block.BlockGrowEvent} happens. This will permit farmers to achieve their job
 * without seeing their action being cancelled by this patch plugin. This is the purpose of the
 * {@link #removeTag(Location)} method.
 *
 * <p>A tag can be placed with {@link #putTag(Location, boolean)} method. The {@link
 * #putBackTagOnMovedBlocks(List, Vector)} has a special purpose: to permit to put back tags when
 * blocks are moved (e.g. {@link org.bukkit.event.block.BlockPistonExtendEvent} and {@link
 * org.bukkit.event.block.BlockPistonRetractEvent}).
 *
 * <p>Finally, this controller give the possibility to check if the jobs ActionType involving a
 * given Block is a place-and-break exploit or no with the method {@link
 * #isPlaceAndBreakAction(ActionType, Location)}.
 *
 * @see ActionType
 * @see Block
 * @see org.bukkit.event.block.BlockBreakEvent BlockBreakEvent
 * @see org.bukkit.event.block.BlockGrowEvent BlockGrowEvent
 * @see org.bukkit.event.block.BlockPistonExtendEvent BlockPistonExtendEvent
 * @see org.bukkit.event.block.BlockPistonRetractEvent BlockPistonRetractEvent
 * @see org.bukkit.event.block.BlockPlaceEvent BlockPlaceEvent
 */
@Singleton
public class PatchPlaceAndBreakJobsController {

  private static final Duration EPHEMERAL_TAG_DURATION = Duration.ofSeconds(3);

  private final LocationConverter locationConverter;
  private final Logger logger;
  private final PatchPlaceAndBreakService patchPlaceAndBreakService;

  /**
   * Constructor.
   *
   * @param locationConverter The location converter.
   * @param logger The Bukkit logger.
   * @param patchPlaceAndBreakService The patch place-and-break service.
   */
  @Inject
  public PatchPlaceAndBreakJobsController(@NotNull LocationConverter locationConverter,
      @NotNull @Named("BukkitLogger") Logger logger,
      @NotNull PatchPlaceAndBreakService patchPlaceAndBreakService) {
    this.locationConverter = locationConverter;
    this.logger = logger;
    this.patchPlaceAndBreakService = patchPlaceAndBreakService;
  }

  /**
   * This method permits to put a {@link PatchPlaceAndBreakTag} on a given block's location.
   *
   * <p>If the tag must be considered as an "ephemeral" one, then a validity duration will be
   * specified to it. The duration is implementation specific, but a good default value could be
   * three seconds.
   *
   * @param location The block's location to be tagged.
   * @param isEphemeral <code>true</code> if the tag must be "ephemeral", <code>false</code>
   *     otherwise.
   * @return void
   */
  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> putTag(@NotNull Location location, boolean isEphemeral) {
    Objects.requireNonNull(location);

    return CompletableFuture.runAsync(() -> {
      TagLocation tagLocation = locationConverter.convert(location);
      patchPlaceAndBreakService.putTag(isEphemeral, tagLocation).join();
    });
  }

  /**
   * This method permits to put back an existing {@link PatchPlaceAndBreakTag} to a moved block,
   * which means that non-tagged block remains non-tagged because no {@link PatchPlaceAndBreakTag}
   * are created by calling this method. The put back action shall be dispatched on next server
   * tick.
   *
   * <p><i>Note: specified blocks are expected to move one block only in the given direction.</i>
   *
   * @param blocks The blocks to be moved after the end of an event processing like {@link
   *     org.bukkit.event.block.BlockPistonExtendEvent}.
   * @param direction The move direction of the specified blocks in order to reattach tags to these
   *     new calculated locations.
   * @return void
   */
  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> putBackTagOnMovedBlocks(@NotNull List<Block> blocks,
      @NotNull Vector direction) {
    Objects.requireNonNull(blocks);
    Objects.requireNonNull(direction);

    return CompletableFuture.runAsync(() -> {
      for (Block block : blocks) {
        Optional<PatchPlaceAndBreakTag> tag = getTag(block.getLocation()).join();

        if (!tag.isPresent()) {
          continue;
        }

        putTag(block.getLocation().add(direction), false).join();
      }
    });
  }

  /**
   * This method permits to remove a tag from a specified Block. This can be useful when the state
   * of the block change (e.g. crops like {@link org.bukkit.Material#WHEAT}).
   *
   * @param location The block's location which will have its tag to be removed if exists.
   * @return void
   */
  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> removeTag(@NotNull Location location) {
    Objects.requireNonNull(location);

    return CompletableFuture.runAsync(() -> {
      TagLocation tagLocation = locationConverter.convert(location);
      patchPlaceAndBreakService.removeTag(tagLocation).join();
    });
  }

  /**
   * This method permit to check if a jobs ActionType with a given Block is a place-and-break
   * exploit or no.
   *
   * <p>This method will return <code>true</code> if all theses following criteria are meet:
   *
   * <ul>
   *   <li>The ActionType is eligible (the list of ActionType eligible is implementation specific,
   *       but we expect the list to contain at least {@link ActionType#BREAK}, {@link
   *       ActionType#TNTBREAK} and {@link ActionType#PLACE}) ;
   *   <li>A "non-ephemeral" tag is attached to the given block or the "ephemeral" one found hasn't
   *       expired.
   * </ul>
   *
   * @param actionType The jobs ActionType.
   * @param location The block's location with a potential place-and-break patch tag.
   * @return <code>true</code> if it's a place-and-break exploit, <code>false</code> otherwise.
   */
  public @NotNull CompletableFuture<Boolean> isPlaceAndBreakAction(@NotNull ActionType actionType,
      @NotNull Location location) {
    Objects.requireNonNull(actionType);
    Objects.requireNonNull(location);

    return CompletableFuture.supplyAsync(() -> {
      Optional<PatchPlaceAndBreakTag> tag = getTag(location).join();

      if (!isActionToPatch(actionType) || !tag.isPresent()) {
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

  /**
   * The purpose of this method is to verify the well-application of the patch if required for a
   * given jobs action. If a place-and-break action has been detected (via a call to {@link
   * #isPlaceAndBreakAction(ActionType, Location)}) but the event still not cancelled then a warning
   * log will be sent.
   *
   * @param actionType The jobs ActionType.
   * @param block The block with a potential place-and-break patch tag.
   * @param isEventCancelled <code>true</code> if the event is cancelled, <code>false</code>
   *     otherwise.
   * @param player The involved player by the patch.
   * @param job The involved job by the patch.
   * @param handlerList The list of event handlers which could be the source of the issue if it exists.
   * @return void
   */
  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> verifyPatchApplication(@NotNull ActionType actionType,
      @NotNull Block block, boolean isEventCancelled, @NotNull OfflinePlayer player,
      @NotNull Job job, @NotNull HandlerList handlerList) {
    Objects.requireNonNull(actionType);
    Objects.requireNonNull(block);
    Objects.requireNonNull(player);
    Objects.requireNonNull(job);
    Objects.requireNonNull(handlerList);

    return CompletableFuture.runAsync(() -> {
      if (isPlaceAndBreakAction(actionType, block.getLocation()).join() && !isEventCancelled) {
        logger.warning(String.format(
            "Violation of a place-and-break patch detected! It's possible that's because of a"
                + " conflict with another plugin. Please, report this full log message to the"
                + " developer: player=%s, jobs=%s, actionType=%s, blockMaterial=%s,"
                + " detectedPotentialConflictingPlugins=%s",
            player.getName(), job.getName(), actionType.getName(), block.getType().name(),
            Arrays.stream(handlerList.getRegisteredListeners())
                .map(registeredListener -> registeredListener.getPlugin().getName()).distinct()
                .collect(Collectors.toList())));
      }
    });
  }

  private @NotNull CompletableFuture<Optional<PatchPlaceAndBreakTag>> getTag(
      @NotNull Location location) {
    TagLocation tagLocation = locationConverter.convert(location);
    return patchPlaceAndBreakService.findTagByLocation(tagLocation);
  }

  private boolean isActionToPatch(@NotNull ActionType actionType) {
    return Arrays.asList(ActionType.BREAK, ActionType.TNTBREAK, ActionType.PLACE)
        .contains(actionType);
  }
}
