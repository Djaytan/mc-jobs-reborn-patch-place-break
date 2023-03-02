/*-
 * #%L
 * JobsReborn-PatchPlaceBreak
 * %%
 * Copyright (C) 2022 - 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 * %%
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
 * #L%
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import java.time.Duration;
import java.util.Collection;
import lombok.NonNull;

/**
 * This interface represents the API to apply place-and-break patch.
 *
 * <p>The patch of JobsReborn plugin works as follows: when an eligible event occurs (e.g. a block
 * place), a tag is attached to the block in order to cancel future jobs rewards (payments,
 * experience and points) when breaking this non-naturally placed block (e.g. preventing diamond
 * ores place-and-break exploit). This will be applied for {@link BlockActionType#BREAK} and {@link
 * BlockActionType#TNTBREAK} jobs actions.
 *
 * <p>It can work in the reverse order: when breaking a block, an "ephemeral" tag is placed to the
 * newly "placed" AIR block. It permits to cancel jobs rewards when placing another block in a
 * short-time delay (e.g. prevent sapling place-and-break exploit). This will be applied for {@link
 * BlockActionType#PLACE} jobs actions.
 *
 * <p>Nevertheless, an attached tag can be removed given specific conditions like when a block grow
 * event happens. This will permit farmers to achieve their job without seeing their action being
 * cancelled by this patch plugin. This is the purpose of the {@link #removeTags(BlockLocation)}
 * method.
 *
 * <p>A tag can be placed with {@link #putTag(BlockLocation, boolean)} method. The {@link
 * #moveTags(Collection, Vector)} has a special purpose: to permit to put back tags when blocks are
 * moved (e.g. by block piston extend and retract events).
 *
 * <p>Finally, this API give the possibility to check if the jobs action type involving a given
 * block is a place-and-break exploit or no with the method {@link
 * #isPlaceAndBreakExploit(BlockActionType, BlockLocation)}.
 */
public interface PatchPlaceBreakApi {

  /** The duration of any ephemeral tag created. */
  Duration EPHEMERAL_TAG_DURATION = Duration.ofSeconds(3);

  /**
   * Puts a {@link Tag} to the given location.
   *
   * <p>If the tag must be considered as an ephemeral one, then a validity duration of {@link
   * #EPHEMERAL_TAG_DURATION} will be applied. After this delay the tag will not be considered as
   * active anymore and will be ignored by {@link #isPlaceAndBreakExploit(BlockActionType,
   * BlockLocation)} method.
   *
   * <p>The method is executed asynchronously for performances purposes.
   *
   * @param blockLocation The block location where the tag must be put.
   * @param isEphemeral <code>true</code> if the tag must be ephemeral, <code>false</code>
   *     otherwise.
   */
  void putTag(@NonNull BlockLocation blockLocation, boolean isEphemeral);

  /**
   * Moves given tags according to the specified direction.
   *
   * <p>For each location a check will be done to ensure an active tag exist or not. If it doesn't
   * exist, then nothing else will be done for the concerned location. In other words: this method
   * will <b>never</b> create tag, at most just update existing ones when they are actives.
   *
   * <p>The method is executed asynchronously for performances purposes.
   *
   * @param blockLocations The locations from where to move existing tags.
   * @param direction The direction where to move existing tags.
   */
  void moveTags(@NonNull Collection<BlockLocation> blockLocations, @NonNull Vector direction);

  /**
   * Removes existing tags from a specified location. This can be useful when the state of the block
   * change (e.g. crops grow like with wheat).
   *
   * <p><i>Note: Even if we expect to have only one existing and activated tag at the same time, it
   * is preferable to remove all the ones associated to the given location in order to prevent any
   * side effect because of any evolution, bug or whatever.
   *
   * <p>The method is executed asynchronously for performances purposes.
   *
   * @param blockLocation The location where to remove tags if existing.
   */
  void removeTags(@NonNull BlockLocation blockLocation);

  /**
   * Checks if the specified block action type at the given location is a place-and-break exploit or
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
   * <p>The method is blocking since the value retrieving depend on data source response time.
   *
   * @param blockActionType The performed action type involving a block.
   * @param blockLocation The location where the action has been performed.
   * @return <code>true</code> if it's a place-and-break exploit, <code>false</code> otherwise.
   */
  boolean isPlaceAndBreakExploit(
      @NonNull BlockActionType blockActionType, @NonNull BlockLocation blockLocation);
}
