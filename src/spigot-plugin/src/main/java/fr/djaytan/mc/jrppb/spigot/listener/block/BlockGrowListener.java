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
package fr.djaytan.mc.jrppb.spigot.listener.block;

import fr.djaytan.mc.jrppb.spigot.adapter.PatchPlaceBreakSpigotAdapterApi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link BlockGrowEvent} listener.
 *
 * <p>When a block grows, the metadata stills remains stored in it, which isn't what we want in the
 * context of the patch. Jobs like "farmer" shall be able to get paid when harvesting crops. So, the
 * idea here is simply to remove the corresponding metadata from the grown blocks.
 */
@Singleton
public class BlockGrowListener implements Listener {

  private final PatchPlaceBreakSpigotAdapterApi patchPlaceBreakSpigotAdapterApi;

  @Inject
  public BlockGrowListener(
      @NotNull PatchPlaceBreakSpigotAdapterApi patchPlaceBreakSpigotAdapterApi) {
    this.patchPlaceBreakSpigotAdapterApi = patchPlaceBreakSpigotAdapterApi;
  }

  /**
   * This method is called when a {@link BlockGrowEvent} is dispatched to remove the potentially
   * existing place-and-break patch tag to the growing block.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The block grow event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockGrow(@NotNull BlockGrowEvent event) {
    Block block = event.getBlock();
    patchPlaceBreakSpigotAdapterApi.removeTag(block);
  }
}
