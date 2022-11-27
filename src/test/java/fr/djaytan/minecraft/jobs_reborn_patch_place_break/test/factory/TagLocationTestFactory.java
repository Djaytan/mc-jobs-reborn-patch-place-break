package fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory;

import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;

public final class TagLocationTestFactory {

  public static final String WORLD_NAME = "testWorld";
  public static final double X = 51.0D;
  public static final double Y = 74.523D;
  public static final double Z = 1245.5D;

  private TagLocationTestFactory() {
    // Static class
  }

  public static @NotNull TagLocation create() {
    return TagLocation.of(WORLD_NAME, X, Y, Z);
  }
}
