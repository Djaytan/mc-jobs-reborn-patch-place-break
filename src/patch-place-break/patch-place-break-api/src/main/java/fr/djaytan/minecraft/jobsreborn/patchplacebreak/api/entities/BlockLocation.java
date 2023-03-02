package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities;

import lombok.NonNull;
import lombok.Value;

/** An immutable and thread-safe block location. */
@Value(staticConstructor = "of")
public class BlockLocation {

  @NonNull String worldName;
  int x;
  int y;
  int z;

  /**
   * Creates a block location from a specified one and a given direction.
   *
   * @param blockLocation The original block location from which to create the new one.
   * @param direction The direction permitting to determine the new block location.
   * @return A new block location from a specified one and a given direction.
   */
  public static @NonNull BlockLocation from(
      @NonNull BlockLocation blockLocation, @NonNull Vector direction) {
    int newX = blockLocation.getX() + direction.getModX();
    int newY = blockLocation.getY() + direction.getModY();
    int newZ = blockLocation.getZ() + direction.getModZ();
    return BlockLocation.of(blockLocation.getWorldName(), newX, newY, newZ);
  }
}
