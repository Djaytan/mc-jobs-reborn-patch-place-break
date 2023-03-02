package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
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
      PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi,
      PatchPlaceBreakVerifier patchPlaceBreakVerifier) {
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
  public void patchOnJobsExpGain(@NonNull JobsExpGainEvent event) {
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
  public void verifyPatchOnJobsExpGain(@NonNull JobsExpGainEvent event) {
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
