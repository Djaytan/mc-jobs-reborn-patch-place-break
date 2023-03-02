package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer;

import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public class BooleanIntegerSerializer implements IntegerSerializer<Boolean> {

  @Override
  public @NonNull Integer serialize(@NonNull Boolean bool) {
    return Boolean.TRUE.equals(bool) ? 1 : 0;
  }

  @Override
  public @NonNull Boolean deserialize(@NonNull Integer integer) {
    return integer == 1;
  }
}
