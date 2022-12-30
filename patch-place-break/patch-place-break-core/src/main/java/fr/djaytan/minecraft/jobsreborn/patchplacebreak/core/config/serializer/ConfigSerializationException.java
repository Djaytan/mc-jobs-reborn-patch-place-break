package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.ConfigException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
class ConfigSerializationException extends ConfigException {

  private static final String UNEXPECTED_SERIALIZATION =
      "Serialization of configs isn't expected to ever happens.";

  public static @NonNull ConfigSerializationException unexpectedSerialization() {
    UnsupportedOperationException e = new UnsupportedOperationException(UNEXPECTED_SERIALIZATION);
    return new ConfigSerializationException(e);
  }
}
