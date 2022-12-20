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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.listener.block;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapter;

/**
 * This class represents a {@link BlockGrowEvent} listener.
 *
 * <p>When a block grow, the metadata stills remains stored in it, which isn't what we want in the
 * context of the patch. Jobs like "farmer" shall be able to get paid when harvesting crops. So, the
 * idea here is simply to remove the corresponding metadata from the grown blocks.
 */
@Singleton
public class BlockGrowListener implements Listener {

  private final PatchPlaceBreakBukkitAdapter patchPlaceBreakBukkitAdapter;

  @Inject
  BlockGrowListener(PatchPlaceBreakBukkitAdapter patchPlaceBreakBukkitAdapter) {
    this.patchPlaceBreakBukkitAdapter = patchPlaceBreakBukkitAdapter;
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
  public void onBlockGrow(BlockGrowEvent event) {
    Location location = event.getBlock().getLocation();
    patchPlaceBreakBukkitAdapter.removeTag(location);
  }
}
