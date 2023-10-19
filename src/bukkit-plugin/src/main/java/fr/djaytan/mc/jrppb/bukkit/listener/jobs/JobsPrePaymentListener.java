/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.mc.jrppb.bukkit.listener.jobs;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import fr.djaytan.mc.jrppb.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link JobsPrePaymentEvent} listener.
 *
 * <p>The purpose of this listener is to cancel payments jobs rewards when the action is considered
 * as a place-and-break one to be patched. This covers both points and incomes.
 */
@Singleton
public class JobsPrePaymentListener implements Listener {

  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;

  @Inject
  public JobsPrePaymentListener(
      @NotNull PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi) {
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
  }

  /**
   * This method is called when a {@link JobsPrePaymentEvent} is dispatched to cancel it if the
   * recorded action is a place-and-break one.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we want to have the final
   * word about the result of this event (there isn't any reason to not cancel a place-and-break
   * action).
   *
   * @param event The job pre-payment event.
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void patchOnJobsPrePayment(@NotNull JobsPrePaymentEvent event) {
    if (patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
        event.getActionInfo(), event.getBlock())) {
      event.setCancelled(true);
    }
  }
}
