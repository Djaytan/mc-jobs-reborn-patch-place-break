/*
 * JobsReborn extension to patch place-break (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakApiBukkitAdapter;

/**
 * This class represents a {@link BlockGrowEvent} listener.
 *
 * <p>When a block grow, the metadata stills remains stored in it, which isn't what we want in the
 * context of the patch. Jobs like "farmer" shall be able to get paid when harvesting crops. So, the
 * idea here is simply to remove the corresponding metadata from the grown blocks.
 *
 * @author Djaytan
 * @see BlockGrowEvent
 * @see Listener
 */
@Singleton
public class BlockGrowListener implements Listener {

  private final PatchPlaceBreakApiBukkitAdapter patchPlaceBreakApiBukkitAdapter;

  @Inject
  public BlockGrowListener(
      @NotNull PatchPlaceBreakApiBukkitAdapter patchPlaceBreakApiBukkitAdapter) {
    this.patchPlaceBreakApiBukkitAdapter = patchPlaceBreakApiBukkitAdapter;
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
    Location location = event.getBlock().getLocation();
    patchPlaceBreakApiBukkitAdapter.removeTag(location);
  }
}
