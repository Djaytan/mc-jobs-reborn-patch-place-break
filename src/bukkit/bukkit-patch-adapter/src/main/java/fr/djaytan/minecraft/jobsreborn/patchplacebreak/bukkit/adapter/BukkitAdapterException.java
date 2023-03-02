package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter;

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;
import org.apache.commons.lang3.StringUtils;

@StandardException(access = AccessLevel.PROTECTED)
public class BukkitAdapterException extends RuntimeException {

  private static final String INVALID_JOB_TYPE =
      "Invalid job action type '%s' specified. Expecting one of the following: %s";

  public static @NonNull BukkitAdapterException invalidJobType(
      @NonNull ActionType invalidJobActionType) {
    String validPatchActionTypes = StringUtils.join(BlockActionType.values(), ", ");
    String message = String.format(INVALID_JOB_TYPE, invalidJobActionType, validPatchActionTypes);
    return new BukkitAdapterException(message);
  }
}
