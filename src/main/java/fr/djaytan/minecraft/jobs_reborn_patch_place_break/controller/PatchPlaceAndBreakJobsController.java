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
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PatchPlaceAndBreakJobsController {

  String PLAYER_BLOCK_PLACED_METADATA_KEY =
      "jobs_reborn.patch_place_break.is_block_placed_by_player";

  boolean isTagged(@NotNull Block block);

  void putTag(@NotNull Block block);

  void putTagOnNextTick(@NotNull Block block);

  void removeTag(@NotNull Block block);

  boolean isPlaceAndBreakAction(@NotNull ActionType actionType, @Nullable Block block);

  void putBackTagOnMovedBlocks(@NotNull List<Block> blocks, @NotNull Vector direction);
}
