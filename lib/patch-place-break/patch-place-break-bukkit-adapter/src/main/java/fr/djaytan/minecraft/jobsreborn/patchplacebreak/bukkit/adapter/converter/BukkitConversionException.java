package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.container.ActionType;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakException;

public class BukkitConversionException extends PatchPlaceBreakException {

  private BukkitConversionException(@NotNull String message) {
    super(message);
  }

  public static @NotNull BukkitConversionException invalidJobType(
      @NotNull ActionType invalidJobActionType) {
    String validPatchActionTypes = getInlineValidActionTypes();
    String message =
        String.format("Invalid job action type '%s' specified. Expecting one of the following: %s",
            invalidJobActionType, validPatchActionTypes);
    return new BukkitConversionException(message);
  }

  private static @NotNull String getInlineValidActionTypes() {
    return StringUtils.join(PatchActionType.values(), ", ");
  }
}
