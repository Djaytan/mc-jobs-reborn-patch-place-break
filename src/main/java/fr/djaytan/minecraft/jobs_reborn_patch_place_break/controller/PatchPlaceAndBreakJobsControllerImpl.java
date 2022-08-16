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

import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import com.google.common.base.Preconditions;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * This class represents the default implementation of {@link PatchPlaceAndBreakJobsController}
 * interface.
 *
 * @author Djaytan
 */
@Singleton
public class PatchPlaceAndBreakJobsControllerImpl implements PatchPlaceAndBreakJobsController {

  private static final Duration EPHEMERAL_TAG_DURATION = Duration.ofSeconds(3);

  private final BukkitScheduler bukkitScheduler;
  private final Logger logger;
  private final Plugin plugin;

  /**
   * Constructor.
   *
   * @param bukkitScheduler The Bukkit scheduler.
   * @param logger The plugin's SLF4J logger.
   * @param plugin The plugin.
   */
  @Inject
  public PatchPlaceAndBreakJobsControllerImpl(
      @NotNull BukkitScheduler bukkitScheduler, @NotNull Logger logger, @NotNull Plugin plugin) {
    this.bukkitScheduler = bukkitScheduler;
    this.logger = logger;
    this.plugin = plugin;
  }

  @Override
  public void putTag(@NotNull Block block, boolean isEphemeral) {
    Preconditions.checkNotNull(block);

    LocalDateTime localDateTime = LocalDateTime.now();
    Duration duration = isEphemeral ? EPHEMERAL_TAG_DURATION : null;

    PatchPlaceAndBreakTag tag = new PatchPlaceAndBreakTag(localDateTime, duration);

    block.setMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY, new FixedMetadataValue(plugin, tag));
  }

  @Override
  public void putTagOnNextTick(@NotNull Block block, boolean isEphemeral) {
    Preconditions.checkNotNull(block);

    Location location = block.getLocation();
    World world = block.getWorld();

    bukkitScheduler.runTaskLater(plugin, () -> putTag(world.getBlockAt(location), isEphemeral), 1L);
  }

  @Override
  public void putBackTagOnMovedBlocks(@NotNull List<Block> blocks, @NotNull Vector direction) {
    Preconditions.checkNotNull(blocks);
    Preconditions.checkNotNull(direction);

    for (Block block : blocks) {
      Optional<PatchPlaceAndBreakTag> tag = getTag(block);

      if (!tag.isPresent()) {
        continue;
      }

      World world = block.getWorld();
      Location newLocation = block.getLocation().add(direction);

      // Wait the piston action to occur and then put back tag
      bukkitScheduler.runTaskLater(
          plugin,
          () ->
              world
                  .getBlockAt(newLocation)
                  .setMetadata(
                      PLAYER_BLOCK_PLACED_METADATA_KEY, new FixedMetadataValue(plugin, tag.get())),
          1L);
    }
  }

  @Override
  public void removeTag(@NotNull Block block) {
    Preconditions.checkNotNull(block);
    block.removeMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY, plugin);
  }

  @Override
  public boolean isPlaceAndBreakAction(@NotNull ActionType actionType, @NotNull Block block) {
    Preconditions.checkNotNull(actionType);
    Preconditions.checkNotNull(block);

    Optional<PatchPlaceAndBreakTag> tag = getTag(block);

    if (!isActionToPatch(actionType) || !tag.isPresent()) {
      return false;
    }

    if (tag.get().getValidityDuration() == null) {
      return true;
    }

    LocalDateTime localDateTime = LocalDateTime.now();
    Duration timeElapsed = Duration.between(tag.get().getInitLocalDateTime(), localDateTime);

    return timeElapsed.minus(EPHEMERAL_TAG_DURATION).isNegative();
  }

  @Override
  public void verifyPatchApplication(
      @NotNull ActionType actionType,
      @NotNull Block block,
      boolean isEventCancelled,
      @NotNull OfflinePlayer player,
      @NotNull Job job,
      @NotNull HandlerList handlerList) {
    Preconditions.checkNotNull(actionType);
    Preconditions.checkNotNull(block);
    Preconditions.checkNotNull(player);
    Preconditions.checkNotNull(job);
    Preconditions.checkNotNull(handlerList);

    if (isPlaceAndBreakAction(actionType, block) && !isEventCancelled) {
      logger.warn(
          "Violation of a place-and-break patch detected! It's possible that's because of a"
              + " conflict with another plugin. Please, report this full log message to the"
              + " developer: player={}, jobs={}, actionType={}, blockMaterial={},"
              + " detectedPotentielConflictingPlugins={}",
          player.getName(),
          job.getName(),
          actionType.getName(),
          block.getType().name(),
          Arrays.stream(handlerList.getRegisteredListeners())
              .map(registeredListener -> registeredListener.getPlugin().getName())
              .distinct()
              .toArray());
    }
  }

  private @NotNull Optional<PatchPlaceAndBreakTag> getTag(@NotNull Block block) {
    Optional<MetadataValue> metadataValue =
        block.getMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY).stream()
            .filter(mv -> Objects.equals(mv.getOwningPlugin(), plugin))
            .findFirst();

    if (!metadataValue.isPresent()) {
      return Optional.empty();
    }

    PatchPlaceAndBreakTag tag = (PatchPlaceAndBreakTag) metadataValue.get().value();
    return Optional.ofNullable(tag);
  }

  private boolean isActionToPatch(@NotNull ActionType actionType) {
    return Arrays.asList(ActionType.BREAK, ActionType.TNTBREAK, ActionType.PLACE)
        .contains(actionType);
  }
}
