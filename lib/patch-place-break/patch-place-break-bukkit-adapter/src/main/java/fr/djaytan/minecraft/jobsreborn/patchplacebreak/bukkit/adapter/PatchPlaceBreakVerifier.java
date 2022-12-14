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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.slf4j.Logger;

import com.gamingmesh.jobs.container.ActionType;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import lombok.NonNull;

@Singleton
public class PatchPlaceBreakVerifier {

  private final Logger logger;
  private final PatchPlaceBreakBukkitAdapter patchPlaceBreakBukkitAdapter;

  @Inject
  public PatchPlaceBreakVerifier(Logger logger,
      PatchPlaceBreakBukkitAdapter patchPlaceBreakBukkitAdapter) {
    this.logger = logger;
    this.patchPlaceBreakBukkitAdapter = patchPlaceBreakBukkitAdapter;
  }

  /**
   * The purpose of this method is to verify the well-application of the patch if required for a
   * given jobs action. If a place-and-break action has been detected (via a call to {@link
   * PatchPlaceBreakBukkitAdapter#isPlaceAndBreakAction(ActionType, Location)}) but the event still
   * not cancelled then a warning log will be sent.
   *
   * @param environmentState The current state of the environment where the patch is supposed to be applied
   * @return void
   */
  @CanIgnoreReturnValue
  public @NonNull CompletableFuture<Void> verifyAppliance(
      @NonNull BukkitPatchEnvironmentState environmentState) {
    return CompletableFuture.runAsync(() -> {
      ActionType jobActionType = environmentState.getJobActionType();
      Location targetedLocation = environmentState.getTargetedBlock().getLocation();

      boolean isPlaceAndBreakAction = patchPlaceBreakBukkitAdapter
          .isPlaceAndBreakAction(jobActionType, targetedLocation).join();
      boolean isEventCancelled = environmentState.isEventCancelled();

      boolean isPatchViolation = isPlaceAndBreakAction && !isEventCancelled;

      if (isPatchViolation) {
        logger.atWarn()
            .log("Violation of a place-and-break patch detected! It's possible that's because of a"
                + " conflict with another plugin. Please, report this full log message to the"
                + " developer: {}", environmentState);
      }
    });
  }
}
