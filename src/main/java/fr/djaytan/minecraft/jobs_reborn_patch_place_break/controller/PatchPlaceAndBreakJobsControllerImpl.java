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
import com.google.common.base.Preconditions;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents the default implementation of {@link PatchPlaceAndBreakJobsController}
 * interface.
 *
 * @author Djaytan
 */
public class PatchPlaceAndBreakJobsControllerImpl implements PatchPlaceAndBreakJobsController {

  private static final Duration EPHEMERAL_TAG_DURATION = Duration.ofSeconds(3);

  private final BukkitScheduler bukkitScheduler;
  private final Plugin plugin;

  /**
   * Constructor.
   *
   * @param bukkitScheduler The Bukkit scheduler.
   * @param plugin The plugin.
   */
  public PatchPlaceAndBreakJobsControllerImpl(
      @NotNull BukkitScheduler bukkitScheduler, @NotNull Plugin plugin) {
    this.bukkitScheduler = bukkitScheduler;
    this.plugin = plugin;
  }

  @Override
  public boolean isPlaceAndBreakAction(@NotNull ActionType actionType, @Nullable Block block) {
    Preconditions.checkNotNull(actionType);

    if (block == null || !isActionToPatch(actionType)) {
      return false;
    }

    Optional<PatchPlaceAndBreakTag> tag = getTag(block);

    if (!tag.isPresent()) {
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
