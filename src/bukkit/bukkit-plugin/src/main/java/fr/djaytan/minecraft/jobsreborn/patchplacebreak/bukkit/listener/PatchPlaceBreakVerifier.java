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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the patch-place-break verifier for the Bukkit plugin.
 *
 * <p>We need to verify the well-appliance of the patch because of conflicts risks with other
 * plugins on a same server. In case the patch has not been applied has expected, then server
 * administrator should be informed of the situation in order to report it to the developer for
 * investigations.
 *
 * <p>Automatic reconcile is not trivial and costly to implement (because of asynchronous mode)
 * without guarantees of success in the end, hence only sending warning message.
 */
@Slf4j
@Singleton
public class PatchPlaceBreakVerifier {

  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;

  @Inject
  public PatchPlaceBreakVerifier(
      @NotNull PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi) {
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
  }

  /**
   * Checks if the patch has been well-applied in case of exploit-detection. If exploit has been
   * detected but patch has not been well-applied in the end, then a warning message is logger for
   * encouraging further investigations.
   *
   * <p>The method is executed asynchronously for performances purposes.
   *
   * @param environmentState The environment state useful to check the well-appliance of the patch.
   * @return The completable future.
   */
  public @NotNull CompletableFuture<Void> checkAndAttemptFixListenersIfRequired(
      @NotNull BukkitPatchEnvironmentState environmentState) {
    return CompletableFuture.runAsync(
        () -> {
          if (isValidState(environmentState)) {
            return;
          }

          log.atWarn()
              .log(
                  "Violation of a place-and-break patch detected! It's possible that's"
                      + " because of a conflict with another plugin. Please, report this full log"
                      + " message to the developer: {}",
                  environmentState);
        });
  }

  private boolean isValidState(@NotNull BukkitPatchEnvironmentState environmentState) {
    return isPatchApplied(environmentState) || !isPatchExpected(environmentState);
  }

  private boolean isPatchApplied(@NotNull BukkitPatchEnvironmentState environmentState) {
    return environmentState.isEventCancelled();
  }

  private boolean isPatchExpected(@NotNull BukkitPatchEnvironmentState environmentState) {
    return patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
        environmentState.getJobActionInfo(), environmentState.getTargetedBlock());
  }
}
