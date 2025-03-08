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
package fr.djaytan.mc.jrppb.paper.listener.block;

import static fr.djaytan.mc.jrppb.paper.listener.block.LogStrippingChangeDetector.isLogStrippingChange;

import fr.djaytan.mc.jrppb.paper.adapter.PatchPlaceBreakPaperAdapterApi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a listener for log stripping.
 *
 * <p>Its purpose is to tolerate payment when breaking a log after having been stripped beforehand.
 * This doesn't question the patch when a stripped log is placed: the regular patch process applies
 * in such a scenario.
 *
 * <p>To summarize:
 *
 * <ul>
 *   <li>When the log block is placed then stripped and finally broken, payment for the break action
 *       is tolerated
 *   <li>When the (stripped) log block is placed then broken, payment is blocked
 * </ul>
 */
@Singleton
public final class LogStrippingListener implements Listener {

  private final PatchPlaceBreakPaperAdapterApi patchPlaceBreakPaperAdapterApi;

  @Inject
  public LogStrippingListener(PatchPlaceBreakPaperAdapterApi patchPlaceBreakPaperAdapterApi) {
    this.patchPlaceBreakPaperAdapterApi = patchPlaceBreakPaperAdapterApi;
  }

  /**
   * This method is called when a {@link EntityChangeBlockEvent} is dispatched to remove the
   * potentially existing place-and-break patch tag to the now stripped log block.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The entity change block event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEntityChangeBlock(@NotNull EntityChangeBlockEvent event) {
    Material initialBlockType = event.getBlock().getType();
    Material finalBlockType = event.getTo();

    if (isLogStrippingChange(initialBlockType, finalBlockType)) {
      patchPlaceBreakPaperAdapterApi.removeTag(event.getBlock());
    }
  }
}
