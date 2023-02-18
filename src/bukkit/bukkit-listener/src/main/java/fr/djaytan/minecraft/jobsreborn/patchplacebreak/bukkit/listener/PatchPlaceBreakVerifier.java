/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener;

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapter;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;

/**
 * Represents the patch-place-break verifier for the Bukkit plugin.
 *
 * <p>After the appliance of a patch (typically done by cancelling the appropriate events from
 * JobsReborn API), a risk that it's ignored because of conflicts with other plugins exists. The
 * idea of this class is then to provide a way to verify the well-application of the patch and try
 * to apply an automatic fix if things go wrong.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PatchPlaceBreakVerifier {

  /*
   * Cyclic dependency: PatchPlaceBreakVerifier -> ListenerRegister
   *  -> a listener (e.g. JobsExpGainVerificationListener) -> PatchPlaceBreakVerifier
   */
  private final Provider<ListenerRegister> listenerRegister;
  private final PatchPlaceBreakBukkitAdapter patchPlaceBreakBukkitAdapter;

  public void checkAndAttemptFixListenersIfRequired(
      @NonNull BukkitPatchEnvironmentState environmentState) {
    CompletableFuture.runAsync(
        () -> {
          if (!isPatchExpected(environmentState).join()) {
            return;
          }

          if (!isPatchApplied(environmentState)) {
            log.atWarn()
                .log(
                    "Violation of a place-and-break patch detected! It's possible that's"
                        + " because of a conflict with another plugin. Tentative to automatically fix the"
                        + " issue on-going... If this warning persists, please, report this full log"
                        + " message to the developer: {}",
                    environmentState);
            listenerRegister.get().reloadListeners();
          }
        });
  }

  private @NonNull CompletableFuture<Boolean> isPatchExpected(
      @NonNull BukkitPatchEnvironmentState environmentState) {
    ActionType jobActionType = environmentState.getJobActionType();
    Location targetedLocation = environmentState.getTargetedBlock().getLocation();
    return patchPlaceBreakBukkitAdapter.isPlaceAndBreakExploit(jobActionType, targetedLocation);
  }

  private boolean isPatchApplied(@NonNull BukkitPatchEnvironmentState environmentState) {
    return environmentState.isEventCancelled();
  }
}
