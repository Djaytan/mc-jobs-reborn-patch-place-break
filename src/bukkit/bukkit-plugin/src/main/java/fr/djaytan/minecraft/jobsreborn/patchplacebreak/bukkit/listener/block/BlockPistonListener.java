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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Collection;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link org.bukkit.event.block.BlockPistonEvent} listener. More
 * specifically, this is a listener of both {@link BlockPistonExtendEvent} and {@link
 * BlockPistonRetractEvent}.
 *
 * <p>The purpose of these listeners are to prevent the piston exploit which can be a way to remove
 * a place-and-break patch tag and this isn't what we want. So, the idea is simply to move tags in
 * the same direction as the moved blocks.
 */
@Singleton
public class BlockPistonListener implements Listener {

  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;

  @Inject
  public BlockPistonListener(
      @NotNull PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi) {
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
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
    Collection<Block> blocks = event.getBlocks();
    patchPlaceBreakBukkitAdapterApi.moveTags(blocks, blockFace);
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
    Collection<Block> blocks = event.getBlocks();
    patchPlaceBreakBukkitAdapterApi.moveTags(blocks, blockFace);
  }
}
