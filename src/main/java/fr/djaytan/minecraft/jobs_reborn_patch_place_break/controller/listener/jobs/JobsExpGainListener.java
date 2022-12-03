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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.jobs;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsController;

/**
 * This class represents a {@link JobsExpGainEvent} listener.
 *
 * <p>The purpose of this listener is to cancel exp-gain jobs rewards when the action is considered
 * as a place-and-break one to be patched.
 *
 * @author Djaytan
 * @see JobsExpGainEvent
 * @see Listener
 */
@Singleton
public class JobsExpGainListener implements Listener {

  private final PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController;

  /**
   * Constructor.
   *
   * @param patchPlaceAndBreakJobsController The place-and-break patch controller.
   */
  @Inject
  public JobsExpGainListener(
      @NotNull PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController) {
    this.patchPlaceAndBreakJobsController = patchPlaceAndBreakJobsController;
  }

  /**
   * This method is called when a {@link JobsExpGainEvent} is dispatched to cancel it if the
   * recorded action is a place-and-break one.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we want to have the final
   * word about the result of this event (a place-and-break action must be cancelled in all cases).
   *
   * @param event The jobs exp-gain event.
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onJobsExpGain(@NotNull JobsExpGainEvent event) {
    Block block = event.getBlock();
    ActionInfo actionInfo = event.getActionInfo();

    if (block == null || actionInfo == null) {
      return;
    }

    ActionType actionType = actionInfo.getType();

    if (actionType == null) {
      return;
    }

    Location blockLocation = block.getLocation();

    if (patchPlaceAndBreakJobsController.isPlaceAndBreakAction(actionType, blockLocation).join()) {
      event.setCancelled(true);
    }
  }
}
