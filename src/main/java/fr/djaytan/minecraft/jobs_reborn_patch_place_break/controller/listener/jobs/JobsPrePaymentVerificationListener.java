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

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsController;

/**
 * This class represents a {@link JobsPrePaymentEvent} listener to verify the well-application of
 * the patch if required.
 *
 * @author Djaytan
 * @see JobsPrePaymentEvent
 * @see Listener
 */
@Singleton
public class JobsPrePaymentVerificationListener implements Listener {

  private final PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController;

  /**
   * Constructor.
   *
   * @param patchPlaceAndBreakJobsController The place-and-break patch controller.
   */
  @Inject
  public JobsPrePaymentVerificationListener(
      @NotNull PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController) {
    this.patchPlaceAndBreakJobsController = patchPlaceAndBreakJobsController;
  }

  /**
   * This method is called when a {@link JobsPrePaymentEvent} is dispatched to verify a
   * place-and-break action have been well-patched. Otherwise, a warning log is sent.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to know if
   * the event has been cancelled or no without modifying its result.
   *
   * @param event The jobs pre-payment event.
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onJobsExpGain(@NotNull JobsPrePaymentEvent event) {
    Block block = event.getBlock();
    ActionInfo actionInfo = event.getActionInfo();

    if (block == null || actionInfo == null) {
      return;
    }

    ActionType actionType = actionInfo.getType();

    if (actionType == null) {
      return;
    }

    OfflinePlayer player = event.getPlayer();
    Job job = event.getJob();
    boolean isEventCancelled = event.isCancelled();
    HandlerList handlerList = event.getHandlers();

    patchPlaceAndBreakJobsController.verifyPatchApplication(actionType, block, isEventCancelled,
        player, job, handlerList);
  }
}
