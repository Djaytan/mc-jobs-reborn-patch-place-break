package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import lombok.NonNull;

public interface UnidirectionalConverter<T, U> {

  @NonNull
  U convert(@NonNull T convertible);
}
