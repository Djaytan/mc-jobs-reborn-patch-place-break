package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
public class PatchPlaceBreakVerifier {

  /*
   * Cyclic dependency: PatchPlaceBreakVerifier -> ListenerRegister
   *  -> a listener (e.g. JobsExpGainVerificationListener) -> PatchPlaceBreakVerifier
   */
  private final Provider<ListenerRegister> listenerRegister;
  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;

  @Inject
  public PatchPlaceBreakVerifier(
      Provider<ListenerRegister> listenerRegister,
      PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi) {
    this.listenerRegister = listenerRegister;
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
  }

  /**
   * Checks if the patch has been well-applied in case of exploit-detection. If exploit has been
   * detected but patch has not been well-applied, then an attempt to fix the issue will be done.
   * Otherwise, nothing will be done.
   *
   * <p>The method is executed asynchronously for performances purposes.
   *
   * @param environmentState The environment state useful to check the well-application of the
   *     patch.
   */
  public void checkAndAttemptFixListenersIfRequired(
      @NonNull BukkitPatchEnvironmentState environmentState) {
    CompletableFuture.runAsync(
        () -> {
          if (!isPatchExpected(environmentState)) {
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

  private boolean isPatchExpected(@NonNull BukkitPatchEnvironmentState environmentState) {
    return patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
        environmentState.getJobActionInfo(), environmentState.getTargetedBlock());
  }

  private boolean isPatchApplied(@NonNull BukkitPatchEnvironmentState environmentState) {
    return environmentState.isEventCancelled();
  }
}
