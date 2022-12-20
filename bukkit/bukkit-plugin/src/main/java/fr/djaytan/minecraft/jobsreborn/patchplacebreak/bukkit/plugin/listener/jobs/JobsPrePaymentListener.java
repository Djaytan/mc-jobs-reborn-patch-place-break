/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.listener.jobs;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapter;

/**
 * This class represents a {@link JobsPrePaymentEvent} listener.
 *
 * <p>The purpose of this listener is to cancel payments jobs rewards when the action is considered
 * as a place-and-break one to be patched. This cover both points and incomes.
 */
@Singleton
public class JobsPrePaymentListener implements Listener {

  private final PatchPlaceBreakBukkitAdapter patchPlaceBreakBukkitAdapter;

  @Inject
  JobsPrePaymentListener(PatchPlaceBreakBukkitAdapter patchPlaceBreakBukkitAdapter) {
    this.patchPlaceBreakBukkitAdapter = patchPlaceBreakBukkitAdapter;
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
  public void onJobsPayment(JobsPrePaymentEvent event) {
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

    if (patchPlaceBreakBukkitAdapter.isPlaceAndBreakAction(actionType, blockLocation).join()) {
      event.setCancelled(true);
    }
  }
}
