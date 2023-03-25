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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.container.ActionInfo;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.BukkitPatchEnvironmentState;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.PatchPlaceBreakVerifier;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link JobsExpGainEvent} listener.
 *
 * <p>The purpose of this listener is to cancel exp-gain jobs rewards when the action is considered
 * as a place-and-break one to be patched.
 */
@Singleton
public class JobsExpGainListener implements Listener {

  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;
  private final PatchPlaceBreakVerifier patchPlaceBreakVerifier;

  @Inject
  public JobsExpGainListener(
      @NotNull PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi,
      @NotNull PatchPlaceBreakVerifier patchPlaceBreakVerifier) {
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
    this.patchPlaceBreakVerifier = patchPlaceBreakVerifier;
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
  public void patchOnJobsExpGain(@NotNull JobsExpGainEvent event) {
    if (patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
        event.getActionInfo(), event.getBlock())) {
      event.setCancelled(true);
    }
  }

  /**
   * This method is called when a {@link JobsExpGainEvent} is dispatched to verify a place-and-break
   * action have been well-patched. Otherwise, a warning log is sent.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to know if
   * the event has been cancelled or no without modifying its result.
   *
   * @param event The jobs exp-gain event.
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void verifyPatchOnJobsExpGain(@NotNull JobsExpGainEvent event) {
    Block block = event.getBlock();
    ActionInfo actionInfo = event.getActionInfo();

    if (block == null || actionInfo == null || actionInfo.getType() == null) {
      return;
    }

    BukkitPatchEnvironmentState environmentState =
        new BukkitPatchEnvironmentState(
            actionInfo,
            block,
            event.getPlayer(),
            event.getJob(),
            event,
            event.isCancelled(),
            event.getHandlers());

    patchPlaceBreakVerifier.checkAndAttemptFixListenersIfRequired(environmentState);
  }
}
