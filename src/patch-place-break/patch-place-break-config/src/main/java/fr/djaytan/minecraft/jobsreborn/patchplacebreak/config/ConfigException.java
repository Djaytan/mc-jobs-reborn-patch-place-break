package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class ConfigException extends RuntimeException {

  private static final String READ_CONFIG =
      "Failed to read config file '%s'. Is config file empty?";

  public static @NonNull ConfigException failedReadingConfig(@NonNull String configFileName) {
    String message = String.format(READ_CONFIG, configFileName);
    return new ConfigException(message);
  }
}
