package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class ConfigSerializationException extends RuntimeException {

  private static final String FAIL_TO_SERIALIZE = "Fail to serialize";
  private static final String FAIL_TO_DESERIALIZE = "Fail to deserialize";

  public static @NonNull ConfigSerializationException failToSerialize() {
    return new ConfigSerializationException(FAIL_TO_SERIALIZE);
  }

  public static @NonNull ConfigSerializationException failToSerialize(@NonNull Throwable cause) {
    return new ConfigSerializationException(FAIL_TO_SERIALIZE, cause);
  }

  public static @NonNull ConfigSerializationException failToDeserialize() {
    return new ConfigSerializationException(FAIL_TO_DESERIALIZE);
  }

  public static @NonNull ConfigSerializationException failToDeserialize(@NonNull Throwable cause) {
    return new ConfigSerializationException(FAIL_TO_DESERIALIZE, cause);
  }
}
