package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer;

import java.util.UUID;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public class UUIDStringSerializer implements StringSerializer<UUID> {

  @Override
  public @NonNull String serialize(@NonNull UUID uuid) {
    return uuid.toString();
  }

  @Override
  public @NonNull UUID deserialize(@NonNull String string) {
    return UUID.fromString(string);
  }
}
