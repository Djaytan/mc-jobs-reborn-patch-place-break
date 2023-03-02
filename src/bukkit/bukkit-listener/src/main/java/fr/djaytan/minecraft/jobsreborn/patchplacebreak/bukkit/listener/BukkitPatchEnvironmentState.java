package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener;

import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.Job;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

/** Represents the Bukkit environment state under which the patch API as been executed. */
@Value
@Builder
public class BukkitPatchEnvironmentState {

  @NonNull ActionInfo jobActionInfo;
  @NonNull Block targetedBlock;
  @NonNull OfflinePlayer involvedPlayer;
  @NonNull Job triggeredJob;
  @NonNull Event eventHandled;
  boolean isEventCancelled;
  @NonNull HandlerList eventHandlers;

  @Override
  public @NonNull String toString() {
    String eventHandlersToString = eventHandlersToString();

    return new ToStringBuilder(this)
        .append("jobActionInfo", jobActionInfo.getType().name())
        .append("targetedBlock", targetedBlock.getType().name())
        .append("targetedLocation", targetedBlock.getLocation())
        .append("involvedPlayer", involvedPlayer.getName())
        .append("triggeredJob", triggeredJob.getName())
        .append("eventHandled", eventHandled)
        .append("isEventCancelled", isEventCancelled)
        .append("eventHandlers", eventHandlersToString)
        .toString();
  }

  private @NonNull String eventHandlersToString() {
    Collection<String> registeredListenersToString =
        Arrays.stream(eventHandlers.getRegisteredListeners())
            .map(BukkitPatchEnvironmentState::registeredListenerToString)
            .distinct()
            .collect(Collectors.toList());

    return new ToStringBuilder(eventHandlers)
        .append("registeredListeners", registeredListenersToString)
        .toString();
  }

  private static @NonNull String registeredListenerToString(
      @NonNull RegisteredListener registeredListener) {
    String listenerClass = registeredListener.getListener().getClass().getName();
    String pluginName = registeredListener.getPlugin().getName();
    String eventPriority = registeredListener.getPriority().name();

    return new ToStringBuilder(registeredListener)
        .append("listenerClass", listenerClass)
        .append("pluginName", pluginName)
        .append("eventPriority", eventPriority)
        .toString();
  }
}
