package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import org.jetbrains.annotations.NotNull;

public interface UnidirectionalConverter<T, U> {

  @NotNull
  U convert(T convertible);
}
