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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import com.google.common.base.MoreObjects;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Represents the Bukkit environment state under which the patch API as been executed.
 */
@Value
@Builder
public class BukkitPatchEnvironmentState {

  @NonNull
  ActionType jobActionType;
  @NonNull
  Block targetedBlock;
  @NonNull
  OfflinePlayer involvedPlayer;
  @NonNull
  Job triggeredJob;
  @NonNull
  Event eventHandled;
  boolean isEventCancelled;
  @NonNull
  HandlerList eventHandlers;

  @Override
  public String toString() {
    String eventHandlersToString = eventHandlersToString();

    return MoreObjects.toStringHelper(this).add("jobActionType", jobActionType.name())
        .add("targetedBlock", targetedBlock.getType().name())
        .add("targetedLocation", targetedBlock.getLocation())
        .add("involvedPlayer", involvedPlayer.getName()).add("triggeredJob", triggeredJob.getName())
        .add("eventHandled", eventHandled).add("isEventCancelled", isEventCancelled)
        .add("eventHandlers", eventHandlersToString).toString();
  }

  private @NonNull String eventHandlersToString() {
    Collection<String> registeredListenersToString =
        Arrays.stream(eventHandlers.getRegisteredListeners())
            .map(BukkitPatchEnvironmentState::registeredListenerToString).distinct()
            .collect(Collectors.toList());

    return MoreObjects.toStringHelper(HandlerList.class)
        .add("registeredListeners", registeredListenersToString).toString();
  }

  private static @NonNull String registeredListenerToString(
      @NonNull RegisteredListener registeredListener) {
    String listenerClass = registeredListener.getListener().getClass().getName();
    String pluginName = registeredListener.getPlugin().getName();
    String eventPriority = registeredListener.getPriority().name();
    return MoreObjects.toStringHelper(RegisteredListener.class).add("listenerClass", listenerClass)
        .add("pluginName", pluginName).add("eventPriority", eventPriority).toString();
  }
}
