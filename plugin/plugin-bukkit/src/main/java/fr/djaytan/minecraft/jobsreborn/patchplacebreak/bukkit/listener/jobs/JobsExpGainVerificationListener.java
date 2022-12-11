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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakApiBukkitAdapter;

/**
 * This class represents a {@link JobsExpGainEvent} listener to verify the well-application of the
 * patch if required.
 */
@Singleton
public class JobsExpGainVerificationListener implements Listener {

  private final PatchPlaceBreakApiBukkitAdapter patchPlaceBreakApiBukkitAdapter;

  @Inject
  public JobsExpGainVerificationListener(
      PatchPlaceBreakApiBukkitAdapter patchPlaceBreakApiBukkitAdapter) {
    this.patchPlaceBreakApiBukkitAdapter = patchPlaceBreakApiBukkitAdapter;
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
  public void onJobsExpGain(JobsExpGainEvent event) {
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

    patchPlaceBreakApiBukkitAdapter.verifyPatchApplication(actionType, block, isEventCancelled,
        player, job, handlerList);
  }
}
