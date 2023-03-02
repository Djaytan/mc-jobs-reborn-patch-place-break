package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import javax.inject.Singleton;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;
import org.bukkit.block.Block;

/** Represents a converter between a {@link Block} and a {@link BlockLocation}. */
@Singleton
public class LocationConverter implements UnidirectionalConverter<Block, BlockLocation> {

  /**
   * Converts a {@link Block} to a {@link BlockLocation}.
   *
   * <p><i>Note: the world value in the block instance shall never be <code>null</code>.</i>
   *
   * @param block The block to convert into a block location.
   * @return The converted block location.
   * @throws NullPointerException if the Bukkit world of the Bukkit location is <code>null</code>.
   */
  @Override
  public @NonNull BlockLocation convert(@NonNull Block block) {
    Validate.notNull(block.getWorld(), "The world is null in the given Bukkit location");

    String worldName = block.getWorld().getName();
    int x = block.getX();
    int y = block.getY();
    int z = block.getZ();
    return BlockLocation.of(worldName, x, y, z);
  }
}
