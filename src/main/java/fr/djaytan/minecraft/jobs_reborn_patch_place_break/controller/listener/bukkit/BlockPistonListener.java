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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.bukkit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsController;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.utils.BukkitUtils;

/**
 * This class represents a {@link org.bukkit.event.block.BlockPistonEvent} listener. More
 * specifically, this is a listener of both {@link BlockPistonExtendEvent} and {@link
 * BlockPistonRetractEvent}.
 *
 * <p>The purpose of these listeners are to prevent the piston exploit which can be a way to remove
 * a place-and-break patch tag and this isn't what we want. So, the idea is simply to move tags in
 * the same direction as the moved blocks.
 *
 * @author Djaytan
 * @see org.bukkit.event.block.BlockPistonEvent
 * @see BlockPistonExtendEvent
 * @see BlockPistonRetractEvent
 * @see Listener
 */
@Singleton
public class BlockPistonListener implements Listener {

  private final BukkitUtils bukkitUtils;
  private final PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController;

  /**
   * Constructor.
   *
   * @param patchPlaceAndBreakJobsController The place-and-break patch controller.
   */
  @Inject
  public BlockPistonListener(@NotNull BukkitUtils bukkitUtils,
      @NotNull PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController) {
    this.bukkitUtils = bukkitUtils;
    this.patchPlaceAndBreakJobsController = patchPlaceAndBreakJobsController;
  }

  /**
   * This method is called when a {@link BlockPistonExtendEvent} is dispatched to move
   * place-and-break patch tag to the new destination of blocks.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The block piston extend event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPistonExtend(@NotNull BlockPistonExtendEvent event) {
    BlockFace blockFace = event.getDirection();
    Vector direction = bukkitUtils.convertToDirection(blockFace);
    patchPlaceAndBreakJobsController.putBackTagOnMovedBlocks(event.getBlocks(), direction);
  }

  /**
   * This method is called when a {@link BlockPistonRetractEvent} is dispatched to move
   * place-and-break patch tag to the new destination of blocks.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The block piston retract event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPistonRetract(@NotNull BlockPistonRetractEvent event) {
    BlockFace blockFace = event.getDirection();
    Vector direction = bukkitUtils.convertToDirection(blockFace);
    patchPlaceAndBreakJobsController.putBackTagOnMovedBlocks(event.getBlocks(), direction);
  }
}
