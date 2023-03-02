package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter;

import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
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
   * @see PatchPlaceBreakApi#putTag(BlockLocation, boolean)
   */
  public void putTag(@NonNull Block block, boolean isEphemeral) {
    BlockLocation blockLocation = locationConverter.convert(block);
    patchPlaceBreakApi.putTag(blockLocation, isEphemeral);
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
    Collection<BlockLocation> blockLocations =
        blocks.stream().map(locationConverter::convert).collect(Collectors.toList());
    Vector vector = blockFaceConverter.convert(blockFace);
    patchPlaceBreakApi.moveTags(blockLocations, vector);
  }

  /**
   * Removes existing tags from the specified location.
   *
   * @param block The block from which to remove the tags if they exist.
   * @see PatchPlaceBreakApi#removeTags(BlockLocation)
   */
  public void removeTags(@NonNull Block block) {
    BlockLocation blockLocation = locationConverter.convert(block);
    patchPlaceBreakApi.removeTags(blockLocation);
  }

  /**
   * Checks if the specified job action for the specified block is a patch-and-break exploit or not.
   *
   * @param actionInfo The job action recorded.
   * @param block The targeted block by job action which has been recorded.
   * @return <code>true</code> if the specified job action for the specified block is a
   *     patch-and-break exploit or not.
   * @see PatchPlaceBreakApi#isPlaceAndBreakExploit(BlockActionType, BlockLocation)
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
    BlockLocation blockLocation = locationConverter.convert(block);
    return patchPlaceBreakApi.isPlaceAndBreakExploit(patchActionType, blockLocation);
  }
}
