package fr.djaytan.minecraft.jobs_reborn_patch_place_break.utils;

import javax.inject.Singleton;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Contains utilities with Bukkit.
 */
@Singleton
public class BukkitUtils {

  /**
   * Converts a block face to a vector to represent a direction.
   *
   * @param blockFace The block face to convert as a direction.
   * @return The vector representing the direction of the specified block face.
   */
  public @NotNull Vector convertToDirection(@NotNull BlockFace blockFace) {
    return new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
  }
}
