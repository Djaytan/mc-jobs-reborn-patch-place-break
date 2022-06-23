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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller;

import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * This interface represents the API to apply place-and-break patch.
 *
 * <p>The patch of JobsReborn plugin works as follows: when an eligible event occurs (e.g. {@link
 * org.bukkit.event.block.BlockPlaceEvent}), a tag with key {@link
 * #PLAYER_BLOCK_PLACED_METADATA_KEY} is attached to the block in order to cancel future jobs
 * rewards (payments, experience and points) when breaking this non-naturally placed block (e.g.
 * preventing diamond ores place-and-break exploit). This will be applied for {@link
 * ActionType#BREAK} & {@link ActionType#TNTBREAK} jobs actions.
 *
 * <p><i>Note: block placed by plugins aren't tagged too.</i>
 *
 * <p>It can work in the reverse order: when breaking a block, an "ephemeral" tag is placed to the
 * newly "placed" {@link org.bukkit.Material#AIR} block. It permits to cancel jobs rewards when
 * placing another block in a short-time delay (e.g. prevent sapling place-and-break exploit). This
 * will be applied for {@link ActionType#PLACE} jobs actions.
 *
 * <p>Nevertheless, an attached tag can be removed given specific conditions like when an {@link
 * org.bukkit.event.block.BlockGrowEvent} happens. This will permit farmers to achieve their job
 * without seeing their action being cancelled by this patch plugin. This is the purpose of the
 * {@link #removeTag(Block)} method.
 *
 * <p>A tag can be placed with {@link #putTag(Block, boolean)} and {@link #putTagOnNextTick(Block,
 * boolean)} methods. The use of the second one is useful when there is the need to wait the end of
 * a processing event (e.g. {@link org.bukkit.event.block.BlockBreakEvent}). The {@link
 * #putBackTagOnMovedBlocks(List, Vector)} has a special purpose: to permit to put back tags when
 * blocks are moved (e.g. {@link org.bukkit.event.block.BlockPistonExtendEvent} & {@link
 * org.bukkit.event.block.BlockPistonRetractEvent}).
 *
 * <p>Finally, this controller give the possibility to check if the jobs ActionType involving a
 * given Block is a place-and-break exploit or no with the method {@link
 * #isPlaceAndBreakAction(ActionType, Block)}.
 *
 * @author Djaytan
 * @see ActionType
 * @see Block
 * @see org.bukkit.event.block.BlockBreakEvent BlockBreakEvent
 * @see org.bukkit.event.block.BlockGrowEvent BlockGrowEvent
 * @see org.bukkit.event.block.BlockPistonExtendEvent BlockPistonExtendEvent
 * @see org.bukkit.event.block.BlockPistonRetractEvent BlockPistonRetractEvent
 * @see org.bukkit.event.block.BlockPlaceEvent BlockPlaceEvent
 */
public interface PatchPlaceAndBreakJobsController {

  /**
   * The key of the {@link PatchPlaceAndBreakTag} when storing this last one in metadata of a Block.
   */
  String PLAYER_BLOCK_PLACED_METADATA_KEY =
      "jobs_reborn.patch_place_break.is_block_placed_by_player";

  /**
   * This method permits to put a {@link PatchPlaceAndBreakTag} as a {@link
   * org.bukkit.metadata.MetadataValue} with the {@link #PLAYER_BLOCK_PLACED_METADATA_KEY} key.
   *
   * <p>If the tag must be considered as an "ephemeral" one, then a validity duration will be
   * specified to it. The duration is implementation specific, but a good default value could be
   * three seconds.
   *
   * @param block The block to be tagged.
   * @param isEphemeral <code>true</code> if the tag must be "ephemeral", <code>false</code>
   *     otherwise.
   */
  void putTag(@NotNull Block block, boolean isEphemeral);

  /**
   * This method works in the same way as {@link #putTag(Block, boolean)} with the particularity to
   * put the tag on the next server tick. This is particularly useful when needing to wait the end
   * of a processing event.
   *
   * @param block The block to be tagged.
   * @param isEphemeral <code>true</code> if the tag must be "ephemeral", <code>false</code>
   *     otherwise.
   */
  void putTagOnNextTick(@NotNull Block block, boolean isEphemeral);

  /**
   * This method permits to put back an existing {@link PatchPlaceAndBreakTag} to a moved block,
   * which means that non-tagged block remains non-tagged because no {@link PatchPlaceAndBreakTag}
   * are created by calling this method. The put back action shall be dispatched on next server
   * tick.
   *
   * <p><i>Note: specified blocks are expected to move one block only in the given direction.</i>
   *
   * @param blocks The blocks to be moved after the end of an event processing like {@link
   *     org.bukkit.event.block.BlockPistonExtendEvent}.
   * @param direction The move direction of the specified blocks in order to reattach tags to these
   *     new calculated locations.
   */
  void putBackTagOnMovedBlocks(@NotNull List<Block> blocks, @NotNull Vector direction);

  /**
   * This method permits to remove a tag from a specified Block. This can be useful when the state
   * of the block change (e.g. crops like {@link org.bukkit.Material#WHEAT}).
   *
   * @param block The block which will have its tag to be removed if exists.
   */
  void removeTag(@NotNull Block block);

  /**
   * This method permit to check if a jobs ActionType with a given Block is a place-and-break
   * exploit or no.
   *
   * <p>This method will return <code>true</code> if all theses following criteria are meet:
   *
   * <ul>
   *   <li>The ActionType is eligible (the list of ActionType eligible is implementation specific,
   *       but we expect the list to contain at least {@link ActionType#BREAK}, {@link
   *       ActionType#TNTBREAK} and {@link ActionType#PLACE}) ;
   *   <li>A "non-ephemeral" tag is attached to the given block or the "ephemeral" one found hasn't
   *       expired.
   * </ul>
   *
   * @param actionType The jobs ActionType.
   * @param block The block with a potential place-and-break patch tag.
   * @return <code>true</code> if it's a place-and-break exploit, <code>false</code> otherwise.
   */
  boolean isPlaceAndBreakAction(@NotNull ActionType actionType, @NotNull Block block);

  /**
   * The purpose of this method is to verify the well-application of the patch if required for a
   * given jobs action. If a place-and-break action has been detected (via a call to {@link
   * #isPlaceAndBreakAction(ActionType, Block)}) but the event still not cancelled then a warning
   * log will be sent.
   *
   * @param actionType The jobs ActionType.
   * @param block The block with a potential place-and-break patch tag.
   * @param isEventCancelled <code>true</code> if the event is cancelled, <code>false</code>
   *     otherwise.
   */
  void verifyPatchApplication(
      @NotNull ActionType actionType,
      @NotNull Block block,
      boolean isEventCancelled,
      @NotNull OfflinePlayer player,
      @NotNull Job job,
      @NotNull HandlerList handlerList);
}
