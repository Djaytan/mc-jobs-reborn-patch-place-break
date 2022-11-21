package fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.PatchPlaceAndBreakTag;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;

public final class PatchPlaceAndBreakTagStubFactory {

  public static final UUID PATCH_UUID = UUID.randomUUID();
  public static final LocalDateTime INIT_LOCAL_DATE_TIME = LocalDateTime.now();
  public static final boolean IS_EPHEMERAL = false;
  public static final TagLocation TAG_LOCATION = TagLocationStubFactory.create();

  private PatchPlaceAndBreakTagStubFactory() {}

  public static @NotNull PatchPlaceAndBreakTag create() {
    return PatchPlaceAndBreakTag.of(PATCH_UUID, INIT_LOCAL_DATE_TIME, IS_EPHEMERAL, TAG_LOCATION);
  }
}
