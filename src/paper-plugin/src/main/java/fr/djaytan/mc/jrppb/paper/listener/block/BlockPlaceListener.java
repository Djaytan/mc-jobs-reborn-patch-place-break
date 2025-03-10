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
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link BlockPlaceEvent} listener.
 *
 * <p>The purpose of this listener is to put a place-and-break tag to newly placed blocks by a
 * player. This permits to prevent place-and-break exploit with diamond ores, for example.
 */
@Singleton
public class BlockPlaceListener implements Listener {

  private final PatchPlaceBreakPaperAdapterApi patchPlaceBreakPaperAdapterApi;

  @Inject
  public BlockPlaceListener(
      @NotNull PatchPlaceBreakPaperAdapterApi patchPlaceBreakPaperAdapterApi) {
    this.patchPlaceBreakPaperAdapterApi = patchPlaceBreakPaperAdapterApi;
  }

  /**
   * This method is called when a {@link BlockPlaceEvent} is dispatched to put the place-and-break
   * patch tag.
   *
   * <p>Important note: block place events are not only dispatched when a block is placed by a
   * player as documented in the class' Javadoc. Therefore, some checks like trying to detect log
   * stripping actions are required to avoid unwanted tags. For instance, when stripping a log then
   * breaking it, the break payment is unexpectedly cancelled if not filtered first.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result. Furthermore,
   * because this plugin will always be enabled before the JobsReborn one (enhance the "depend"
   * plugin.yml line), we have the guarantee that this listener will always be called before the one
   * registered by JobsReborn at the same priority level.
   *
   * @param event The block place event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlace(@NotNull BlockPlaceEvent event) {
    Block placedBlock = event.getBlockPlaced();

    if (!isLogStrippingChange(event.getItemInHand(), placedBlock.getType())) {
      patchPlaceBreakPaperAdapterApi.putTag(placedBlock, false);
    }
  }
}
