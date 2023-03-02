package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.BukkitAdapterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public class ActionTypeConverter implements UnidirectionalConverter<ActionType, BlockActionType> {

  @Override
  public @NonNull BlockActionType convert(@NonNull ActionType jobActionType) {
    if (!isValidJobActionType(jobActionType)) {
      throw BukkitAdapterException.invalidJobType(jobActionType);
    }
    return BlockActionType.valueOf(jobActionType.name());
  }

  private boolean isValidJobActionType(@NonNull ActionType jobActionType) {
    Collection<ActionType> validJobActionTypes = getValidJobActionTypes();
    return validJobActionTypes.contains(jobActionType);
  }

  private @NonNull Collection<ActionType> getValidJobActionTypes() {
    return Arrays.stream(BlockActionType.values())
        .map(BlockActionType::name)
        .map(ActionType::valueOf)
        .collect(Collectors.toList());
  }
}
