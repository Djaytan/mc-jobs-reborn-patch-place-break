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
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatchPlaceAndBreakJobsControllerImpl implements PatchPlaceAndBreakJobsController {

  private final BukkitScheduler bukkitScheduler;
  private final Plugin plugin;

  public PatchPlaceAndBreakJobsControllerImpl(
      @NotNull BukkitScheduler bukkitScheduler, @NotNull Plugin plugin) {
    this.bukkitScheduler = bukkitScheduler;
    this.plugin = plugin;
  }

  @Override
  public boolean isTagged(@NotNull Block block) {
    return block.hasMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY);
  }

  @Override
  public boolean isPlaceAndBreakAction(@NotNull ActionType actionType, @Nullable Block block) {
    Preconditions.checkNotNull(actionType);

    if (block == null
        || !actionType.equals(ActionType.BREAK)
            && !actionType.equals(ActionType.TNTBREAK)
            && !actionType.equals(ActionType.PLACE)) {
      return false;
    }

    return block.hasMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY);
  }

  @Override
  public void putTag(@NotNull Block block) {
    Preconditions.checkNotNull(block);
    block.setMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY, new FixedMetadataValue(plugin, true));
  }

  @Override
  public void putTagOnNextTick(@NotNull Block block) {
    Preconditions.checkNotNull(block);

    Location location = block.getLocation();
    World world = block.getWorld();

    bukkitScheduler.runTaskLater(plugin, () -> putTag(world.getBlockAt(location)), 1L);
  }

  @Override
  public void removeTag(@NotNull Block block) {
    Preconditions.checkNotNull(block);
    block.removeMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY, plugin);
  }

  @Override
  public void putBackTagOnMovedBlocks(@NotNull List<Block> blocks, @NotNull Vector direction) {
    for (Block block : blocks) {
      if (!isTagged(block)) {
        continue;
      }

      World world = block.getWorld();
      Location newLocation = block.getLocation().add(direction);

      bukkitScheduler.runTaskLater(plugin, () -> putTag(world.getBlockAt(newLocation)), 1L);
    }
  }
}
