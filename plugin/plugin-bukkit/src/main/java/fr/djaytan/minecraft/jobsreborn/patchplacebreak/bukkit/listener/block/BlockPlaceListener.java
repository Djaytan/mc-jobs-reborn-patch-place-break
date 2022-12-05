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
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakApiBukkitAdapter;

/**
 * This class represents a {@link BlockPlaceEvent} listener.
 *
 * <p>The purpose of this listener is to put a place-and-break tag to newly placed blocks by a
 * player. This permits to prevent place-and-break exploit with diamond ores for example.
 *
 * @author Djaytan
 * @see BlockPlaceEvent
 * @see Listener
 */
@Singleton
public class BlockPlaceListener implements Listener {

  private final PatchPlaceBreakApiBukkitAdapter patchPlaceBreakApiBukkitAdapter;

  @Inject
  public BlockPlaceListener(
      @NotNull PatchPlaceBreakApiBukkitAdapter patchPlaceBreakApiBukkitAdapter) {
    this.patchPlaceBreakApiBukkitAdapter = patchPlaceBreakApiBukkitAdapter;
  }

  /**
   * This method is called when a {@link BlockPlaceEvent} is dispatched to put the place-and-break
   * patch tag.
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
    Location location = event.getBlockPlaced().getLocation();
    patchPlaceBreakApiBukkitAdapter.putTag(location, false);
  }
}
