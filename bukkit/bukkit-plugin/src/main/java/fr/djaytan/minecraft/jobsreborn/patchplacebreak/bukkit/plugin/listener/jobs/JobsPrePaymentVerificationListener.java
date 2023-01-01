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

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.BukkitPatchEnvironmentState;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.PatchPlaceBreakVerifier;

/**
 * This class represents a {@link JobsPrePaymentEvent} listener to verify the well-application of
 * the patch if required.
 */
@Singleton
public class JobsPrePaymentVerificationListener implements Listener {

  private final PatchPlaceBreakVerifier patchPlaceBreakVerifier;

  @Inject
  JobsPrePaymentVerificationListener(PatchPlaceBreakVerifier patchPlaceBreakVerifier) {
    this.patchPlaceBreakVerifier = patchPlaceBreakVerifier;
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
  public void onJobsExpGain(JobsPrePaymentEvent event) {
    Block block = event.getBlock();
    ActionInfo actionInfo = event.getActionInfo();

    if (block == null || actionInfo == null) {
      return;
    }

    ActionType actionType = actionInfo.getType();

    if (actionType == null) {
      return;
    }

    BukkitPatchEnvironmentState environmentState =
        BukkitPatchEnvironmentState.builder().jobActionType(actionType).targetedBlock(block)
            .involvedPlayer(event.getPlayer()).triggeredJob(event.getJob()).eventHandled(event)
            .isEventCancelled(event.isCancelled()).eventHandlers(event.getHandlers()).build();

    patchPlaceBreakVerifier.checkAndAttemptFixListenersIfRequired(environmentState);
  }
}