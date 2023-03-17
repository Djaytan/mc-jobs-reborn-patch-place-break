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

import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.Job;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

/** Represents the Bukkit environment state under which the patch API as been executed. */
@Getter
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
