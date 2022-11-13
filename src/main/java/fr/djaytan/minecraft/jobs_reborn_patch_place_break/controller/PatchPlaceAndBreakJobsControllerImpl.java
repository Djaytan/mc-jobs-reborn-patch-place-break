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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import com.google.common.base.Preconditions;
import com.google.inject.name.Named;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.PatchPlaceAndBreakTag;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.service.PatchPlaceAndBreakService;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.utils.LocationConverter;

/**
 * This class represents the default implementation of {@link PatchPlaceAndBreakJobsController}
 * interface.
 *
 * @author Djaytan
 */
@Singleton
public class PatchPlaceAndBreakJobsControllerImpl implements PatchPlaceAndBreakJobsController {

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
  public PatchPlaceAndBreakJobsControllerImpl(@NotNull LocationConverter locationConverter,
      @NotNull @Named("BukkitLogger") Logger logger,
      @NotNull PatchPlaceAndBreakService patchPlaceAndBreakService) {
    this.locationConverter = locationConverter;
    this.logger = logger;
    this.patchPlaceAndBreakService = patchPlaceAndBreakService;
  }

  @Override
  public @NotNull CompletableFuture<Void> putTag(@NotNull Location location, boolean isEphemeral) {
    Preconditions.checkNotNull(location);

    return CompletableFuture.runAsync(() -> {
      TagLocation tagLocation = locationConverter.convert(location);
      patchPlaceAndBreakService.putTag(isEphemeral, tagLocation).join();
    });
  }

  @Override
  public @NotNull CompletableFuture<Void> putBackTagOnMovedBlocks(@NotNull List<Block> blocks,
      @NotNull Vector direction) {
    Preconditions.checkNotNull(blocks);
    Preconditions.checkNotNull(direction);

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

  @Override
  public @NotNull CompletableFuture<Void> removeTag(@NotNull Location location) {
    Preconditions.checkNotNull(location);

    return CompletableFuture.runAsync(() -> {
      TagLocation tagLocation = locationConverter.convert(location);
      patchPlaceAndBreakService.removeTag(tagLocation).join();
    });
  }

  @Override
  public @NotNull CompletableFuture<Boolean> isPlaceAndBreakAction(@NotNull ActionType actionType,
      @NotNull Location location) {
    Preconditions.checkNotNull(actionType);
    Preconditions.checkNotNull(location);

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

  @Override
  public @NotNull CompletableFuture<Void> verifyPatchApplication(@NotNull ActionType actionType,
      @NotNull Block block, boolean isEventCancelled, @NotNull OfflinePlayer player,
      @NotNull Job job, @NotNull HandlerList handlerList) {
    Preconditions.checkNotNull(actionType);
    Preconditions.checkNotNull(block);
    Preconditions.checkNotNull(player);
    Preconditions.checkNotNull(job);
    Preconditions.checkNotNull(handlerList);

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
