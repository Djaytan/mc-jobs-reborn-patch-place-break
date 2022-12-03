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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.listener.bukkit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.PatchPlaceAndBreakJobsApi;

/**
 * This class represents a {@link BlockSpreadEvent} listener.
 *
 * <p>The purpose of this listener is to removed potentially existing place-and-break tag to spread
 * blocks (e.g. we consider that a player-placed dirt block becoming a grass by spreading event
 * shall result to a successful job action when breaking it).
 *
 * @author Djaytan
 * @see BlockSpreadEvent
 * @see Listener
 */
@Singleton
public class BlockSpreadListener implements Listener {

  private final PatchPlaceAndBreakJobsApi patchPlaceAndBreakJobsApi;

  /**
   * Constructor.
   *
   * @param patchPlaceAndBreakJobsApi The place-and-break patch controller.
   */
  @Inject
  public BlockSpreadListener(
      @NotNull PatchPlaceAndBreakJobsApi patchPlaceAndBreakJobsApi) {
    this.patchPlaceAndBreakJobsApi = patchPlaceAndBreakJobsApi;
  }

  /**
   * This method is called when a {@link BlockSpreadEvent} is dispatched to remove the potentially
   * existing place-and-break patch tag to the spreading block.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The block spread event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockSpread(@NotNull BlockSpreadEvent event) {
    patchPlaceAndBreakJobsApi.removeTag(event.getBlock().getLocation());
  }
}
