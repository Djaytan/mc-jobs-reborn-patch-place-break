package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import javax.inject.Singleton;
import lombok.NonNull;
import org.bukkit.block.BlockFace;

@Singleton
public class BlockFaceConverter implements UnidirectionalConverter<BlockFace, Vector> {

  @Override
  public @NonNull Vector convert(@NonNull BlockFace blockFace) {
    int modX = blockFace.getModX();
    int modY = blockFace.getModY();
    int modZ = blockFace.getModZ();
    return Vector.of(modX, modY, modZ);
  }
}
