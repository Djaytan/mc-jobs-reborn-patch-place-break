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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link BlockGrowEvent} listener.
 *
 * <p>When a block grow, the metadata stills remains stored in it, which isn't what we want in the
 * context of the patch. Jobs like "farmer" shall be able to get paid when harvesting crops. So, the
 * idea here is simply to remove the corresponding metadata from the grown blocks.
 *
 * @author Djaytan
 * @see BlockGrowEvent
 * @see Listener
 */
@Singleton
public class BlockGrowListener implements Listener {

  private final PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController;

  /**
   * Constructor.
   *
   * @param patchPlaceAndBreakJobsController The place-and-break patch controller.
   */
  @Inject
  public BlockGrowListener(
      @NotNull PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController) {
    this.patchPlaceAndBreakJobsController = patchPlaceAndBreakJobsController;
  }

  /**
   * This method is called when a {@link BlockGrowEvent} is dispatched to remove the potentially
   * existing place-and-break patch tag to the growing block.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The block grow event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockGrow(@NotNull BlockGrowEvent event) {
    patchPlaceAndBreakJobsController.removeTag(event.getBlock());
  }
}
