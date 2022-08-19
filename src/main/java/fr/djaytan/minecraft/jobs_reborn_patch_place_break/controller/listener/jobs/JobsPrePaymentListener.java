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

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsController;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link JobsPrePaymentEvent} listener.
 *
 * <p>The purpose of this listener is to cancel payments jobs rewards when the action is considered
 * as a place-and-break one to be patched. This cover both points and incomes.
 *
 * @author Djaytan
 * @see JobsPrePaymentEvent
 * @see Listener
 */
@Singleton
public class JobsPrePaymentListener implements Listener {

  private final PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController;

  /**
   * Constructor.
   *
   * @param patchPlaceAndBreakJobsController The place-and-break patch controller.
   */
  @Inject
  public JobsPrePaymentListener(
      @NotNull PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController) {
    this.patchPlaceAndBreakJobsController = patchPlaceAndBreakJobsController;
  }

  /**
   * This method is called when a {@link JobsPrePaymentEvent} is dispatched to cancel it if the
   * recorded action is a place-and-break one.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we want to have the final
   * word about the result of this event (there isn't any reason to not cancel a place-and-break
   * action).
   *
   * @param event The jobs pre-payment event.
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onJobsPayment(@NotNull JobsPrePaymentEvent event) {
    if (event.getBlock() == null
        || event.getActionInfo() == null
        || event.getActionInfo().getType() == null) {
      return;
    }

    if (patchPlaceAndBreakJobsController.isPlaceAndBreakAction(
        event.getActionInfo().getType(), event.getBlock().getLocation())) {
      event.setCancelled(true);
    }
  }
}
