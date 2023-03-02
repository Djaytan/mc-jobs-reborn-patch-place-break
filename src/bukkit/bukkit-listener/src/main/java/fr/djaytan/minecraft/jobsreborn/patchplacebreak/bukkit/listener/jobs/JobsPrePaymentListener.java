package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.gamingmesh.jobs.container.ActionInfo;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.BukkitPatchEnvironmentState;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.PatchPlaceBreakVerifier;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * This class represents a {@link JobsPrePaymentEvent} listener.
 *
 * <p>The purpose of this listener is to cancel payments jobs rewards when the action is considered
 * as a place-and-break one to be patched. This cover both points and incomes.
 */
@Singleton
public class JobsPrePaymentListener implements Listener {

  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;
  private final PatchPlaceBreakVerifier patchPlaceBreakVerifier;

  @Inject
  public JobsPrePaymentListener(
      PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi,
      PatchPlaceBreakVerifier patchPlaceBreakVerifier) {
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
    this.patchPlaceBreakVerifier = patchPlaceBreakVerifier;
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
  public void patchOnJobsPrePayment(@NonNull JobsPrePaymentEvent event) {
    if (patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
        event.getActionInfo(), event.getBlock())) {
      event.setCancelled(true);
    }
  }

  /**
   * This method is called when a {@link JobsPrePaymentEvent} is dispatched to verify a
   * place-and-break action have been well-patched. Otherwise, a warning log is sent.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to know if
   * the event has been cancelled or not without modifying its result.
   *
   * @param event The jobs pre-payment event.
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void verifyPatchOnJobsPrePayment(@NonNull JobsPrePaymentEvent event) {
    Block block = event.getBlock();
    ActionInfo actionInfo = event.getActionInfo();

    if (block == null || actionInfo == null || actionInfo.getType() == null) {
      return;
    }

    BukkitPatchEnvironmentState environmentState =
        BukkitPatchEnvironmentState.builder()
            .jobActionInfo(actionInfo)
            .targetedBlock(block)
            .involvedPlayer(event.getPlayer())
            .triggeredJob(event.getJob())
            .eventHandled(event)
            .isEventCancelled(event.isCancelled())
            .eventHandlers(event.getHandlers())
            .build();

    patchPlaceBreakVerifier.checkAndAttemptFixListenersIfRequired(environmentState);
  }
}
