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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.bukkit;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a listener of {@link BlockGrowEvent}.
 *
 * <p>When a block grow, the metadata stills remains stored in it, which isn't what we want in the
 * context of the patch. Jobs like "farmer" shall be able to get paid when harvesting crops. So, the
 * idea here is simply to remove the corresponding metadata from the grown blocks.
 *
 * @see Listener
 * @see BlockGrowEvent
 */
public class BlockGrowListener implements Listener {

  private final PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController;

  public BlockGrowListener(
      @NotNull PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController) {
    this.patchPlaceAndBreakJobsController = patchPlaceAndBreakJobsController;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockGrow(@NotNull BlockGrowEvent event) {
    patchPlaceAndBreakJobsController.removeTag(event.getBlock());
  }
}
