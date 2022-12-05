package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.container.ActionType;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchActionType;

public class ActionTypeConverter implements UnidirectionalConverter<ActionType, PatchActionType> {

  @Override
  public @NotNull PatchActionType convert(@NotNull ActionType jobActionType) {
    Objects.requireNonNull(jobActionType);

    if (!isValidJobActionType(jobActionType)) {
      throw BukkitConversionException.invalidJobType(jobActionType);
    }
    return PatchActionType.valueOf(jobActionType.name());
  }

  private boolean isValidJobActionType(ActionType jobActionType) {
    List<ActionType> validJobActionTypes = getValidJobActionTypes();
    return validJobActionTypes.contains(jobActionType);
  }

  private @NotNull List<ActionType> getValidJobActionTypes() {
    return Arrays.stream(PatchActionType.values()).map(PatchActionType::name)
        .map(ActionType::valueOf).collect(Collectors.toList());
  }
}
