package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import java.util.Objects;

import javax.inject.Singleton;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagVector;

@Singleton
public class BlockFaceConverter implements UnidirectionalConverter<BlockFace, TagVector> {

  @Override
  public @NotNull TagVector convert(@NotNull BlockFace blockFace) {
    Objects.requireNonNull(blockFace);

    double modX = blockFace.getModX();
    double modY = blockFace.getModY();
    double modZ = blockFace.getModZ();
    return TagVector.of(modX, modY, modZ);
  }
}
