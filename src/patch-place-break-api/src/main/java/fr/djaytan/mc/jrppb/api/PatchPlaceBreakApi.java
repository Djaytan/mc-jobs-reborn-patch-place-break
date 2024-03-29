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
package fr.djaytan.mc.jrppb.api;

import fr.djaytan.mc.jrppb.api.entities.Block;
import fr.djaytan.mc.jrppb.api.entities.BlockActionType;
import fr.djaytan.mc.jrppb.api.entities.Tag;
import fr.djaytan.mc.jrppb.api.entities.Vector;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

/**
 * This interface represents the API to apply a place-and-break patch.
 *
 * <p>The patch of JobsReborn plugin works as follows: when an eligible event occurs (e.g. a block
 * place), a tag is attached to the block in order to cancel future jobs rewards (payments,
 * experience and points) when breaking this non-naturally placed block (e.g. preventing diamond
 * ores place-and-break exploit). This will be applied for {@link BlockActionType#BREAK} and {@link
 * BlockActionType#TNTBREAK} jobs actions.
 *
 * <p>It can work in the reverse order: when breaking a block, an "ephemeral" tag is placed to the
 * newly "placed" AIR block. It permits canceling jobs rewards when placing another block in a
 * short-time delay (e.g. prevent sapling place-and-break exploit). This will be applied for {@link
 * BlockActionType#PLACE} jobs actions.
 *
 * <p>Nevertheless, an attached tag can be removed given specific conditions like when a block grows
 * event happens. This will permit farmers to achieve their job without seeing their action being
 * cancelled by this patch plugin. This is the purpose of the {@link #removeTag(Block)} method.
 *
 * <p>A tag can be placed with {@link #putTag(Block, boolean)} method. The {@link #moveTags(Set,
 * Vector)} has a special purpose: to permit putting back tags when blocks are moved (e.g. by block
 * piston extend and retract events).
 *
 * <p>Finally, this API gives the possibility to check if the job action type involving a given
 * block is a place-and-break exploit or no with the method {@link
 * #isPlaceAndBreakExploit(BlockActionType, Block)}.
 */
public interface PatchPlaceBreakApi {

  /** The duration of any ephemeral tag created. */
  Duration EPHEMERAL_TAG_DURATION = Duration.ofSeconds(3);

  /**
   * Puts a {@link Tag} to the given location.
   *
   * <p>If the tag must be considered as an ephemeral one, then a validity duration of {@link
   * #EPHEMERAL_TAG_DURATION} will be applied. After this delay the tag will not be considered as
   * active anymore and will be ignored by {@link #isPlaceAndBreakExploit(BlockActionType, Block)}
   * method.
   *
   * <p>The method is executed asynchronously for performance purposes.
   *
   * @param block The block where the tag must be put.
   * @param isEphemeral <code>true</code> if the tag must be ephemeral, <code>false</code>
   *     otherwise.
   * @return The completable future object.
   */
  @NotNull
  CompletableFuture<Void> putTag(@NotNull Block block, boolean isEphemeral);

  /**
   * Moves given tags according to the specified direction.
   *
   * <p>For each location a check will be done to ensure an active tag exist or not. If it doesn't
   * exist, then nothing else will be done for the concerned location. In other words: this method
   * will <b>never</b> create tag, at most update existing ones when they are actives.
   *
   * <p>The method is executed asynchronously for performance purposes.
   *
   * @param blocks The blocks from where to move existing tags.
   * @param direction The direction where to move existing tags.
   * @return The completable future object.
   */
  @NotNull
  CompletableFuture<Void> moveTags(@NotNull Set<Block> blocks, @NotNull Vector direction);

  /**
   * Removes existing tag from a specified block. This can be useful when the state of the block
   * changes (e.g. crops grow like with wheat).
   *
   * <p>The method is executed asynchronously for performance purposes.
   *
   * @param block The block where to remove tags if existing.
   * @return The completable future object.
   */
  @NotNull
  CompletableFuture<Void> removeTag(@NotNull Block block);

  /**
   * Checks if the specified block action type on the given block is a place-and-break exploit or
   * not.
   *
   * <p>This method will return <code>true</code> if one of the following tags exist:
   *
   * <ul>
   *   <li>A non-ephemeral tag
   *   <li>An ephemeral one which hasn't yet expired (i.e. for which lifetime has not exceed {@link
   *       #EPHEMERAL_TAG_DURATION})
   * </ul>
   *
   * <p>The method is blocking since the value retrieving depends on data source response time.
   *
   * @param blockActionType The performed action type involving a block.
   * @param block The block on which the action has been performed.
   * @return <code>true</code> if it's a place-and-break exploit, <code>false</code> otherwise.
   */
  boolean isPlaceAndBreakExploit(@NotNull BlockActionType blockActionType, @NotNull Block block);
}
