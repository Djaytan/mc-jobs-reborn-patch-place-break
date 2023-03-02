package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer;

import lombok.NonNull;

public interface Serializer<T, U> {

  @NonNull
  U serialize(@NonNull T object);

  @NonNull
  T deserialize(@NonNull U object);
}
