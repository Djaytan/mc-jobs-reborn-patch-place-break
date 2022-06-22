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
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JobsControllerImpl implements JobsController {

  private final Plugin plugin;

  public JobsControllerImpl(@NotNull Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean isPlaceAndBreakAction(@NotNull ActionType actionType, @Nullable Block block) {
    Preconditions.checkNotNull(actionType);

    if (block == null
        || !actionType.equals(ActionType.BREAK) && !actionType.equals(ActionType.TNTBREAK)) {
      return false;
    }

    return block.hasMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY);
  }

  @Override
  public void setPlayerBlockPlacedMetadata(@NotNull Block block) {
    Preconditions.checkNotNull(block);
    block.setMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY, new FixedMetadataValue(plugin, true));
  }

  @Override
  public void removePlayerBlockPlacedMetadata(@NotNull Block block) {
    Preconditions.checkNotNull(block);
    block.removeMetadata(PLAYER_BLOCK_PLACED_METADATA_KEY, plugin);
  }
}
