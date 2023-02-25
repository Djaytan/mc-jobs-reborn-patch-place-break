/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter;

import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Location;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.ActionTypeConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.BlockFaceConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.LocationConverter;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Adapter of {@link PatchPlaceBreakApi} for Bukkit.
 *
 * @see PatchPlaceBreakApi
 */
@Singleton
public class PatchPlaceBreakBukkitAdapterApi {

  private final ActionTypeConverter actionTypeConverter;
  private final LocationConverter locationConverter;
  private final PatchPlaceBreakApi patchPlaceBreakApi;
  private final BlockFaceConverter blockFaceConverter;

  @Inject
  public PatchPlaceBreakBukkitAdapterApi(
      ActionTypeConverter actionTypeConverter,
      LocationConverter locationConverter,
      PatchPlaceBreakApi patchPlaceBreakApi,
      BlockFaceConverter blockFaceConverter) {
    this.actionTypeConverter = actionTypeConverter;
    this.locationConverter = locationConverter;
    this.patchPlaceBreakApi = patchPlaceBreakApi;
    this.blockFaceConverter = blockFaceConverter;
  }

  /**
   * Puts a tag on the specified location.
   *
   * @param block The block where to put tag.
   * @param isEphemeral Whether the tag to put must be an ephemeral one or not.
   * @see PatchPlaceBreakApi#putTag(Location, boolean)
   */
  public void putTag(@NonNull Block block, boolean isEphemeral) {
    Location location = locationConverter.convert(block);
    patchPlaceBreakApi.putTag(location, isEphemeral);
  }

  /**
   * Moves tags associated with given blocks (if they exist) to the direction of the specified block
   * face.
   *
   * <p><i>The performed move is of only one block in the specified direction.
   *
   * @param blocks The list of blocks with potential tags to be moved.
   * @param blockFace The block face from which to infer the move direction.
   * @see PatchPlaceBreakApi#moveTags(Collection, Vector)
   */
  public void moveTags(@NonNull Collection<Block> blocks, @NonNull BlockFace blockFace) {
    Collection<Location> locations =
        blocks.stream().map(locationConverter::convert).collect(Collectors.toList());
    Vector vector = blockFaceConverter.convert(blockFace);
    patchPlaceBreakApi.moveTags(locations, vector);
  }

  /**
   * Removes existing tags from the specified location.
   *
   * @param block The block from which to remove the tags if they exist.
   * @see PatchPlaceBreakApi#removeTags(Location)
   */
  public void removeTags(@NonNull Block block) {
    Location location = locationConverter.convert(block);
    patchPlaceBreakApi.removeTags(location);
  }

  /**
   * Checks if the specified job action for the specified block is a patch-and-break exploit or not.
   *
   * @param actionInfo The job action recorded.
   * @param block The targeted block by job action which has been recorded.
   * @return <code>true</code> if the specified job action for the specified block is a
   *     patch-and-break exploit or not.
   * @see PatchPlaceBreakApi#isPlaceAndBreakExploit(BlockActionType, Location)
   */
  public boolean isPlaceAndBreakExploit(ActionInfo actionInfo, Block block) {
    if (actionInfo == null || block == null) {
      return false;
    }

    ActionType actionType = actionInfo.getType();

    if (actionType == null) {
      return false;
    }

    BlockActionType patchActionType = actionTypeConverter.convert(actionType);
    Location location = locationConverter.convert(block);
    return patchPlaceBreakApi.isPlaceAndBreakExploit(patchActionType, location);
  }
}
