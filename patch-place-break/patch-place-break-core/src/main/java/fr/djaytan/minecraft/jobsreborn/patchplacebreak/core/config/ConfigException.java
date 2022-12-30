package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config;

import java.nio.file.Path;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakCoreException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class ConfigException extends PatchPlaceBreakCoreException {

  private static final String CREATE_DEFAULT = "Failed to create default config file '%s'.";
  private static final String READ_CONFIG = "Failed to read config file '%s'.";
  private static final String RESOURCE_NOT_FOUND = "Resource '%s' not found in classpath.";
  private static final String UNEXPECTED_EMPTY_DEFAULT_CONFIG =
      "The default config file '%s' is empty: this is unexpected."
          + " Please contact the developer about this.";

  public static @NonNull ConfigException failedCreatingDefault(@NonNull String configFileName,
      @NonNull Throwable cause) {
    String message = String.format(CREATE_DEFAULT, configFileName);
    return new ConfigException(message, cause);
  }

  public static @NonNull ConfigException failedReadingConfig(@NonNull Path configFile) {
    String message = String.format(READ_CONFIG, configFile);
    return new ConfigException(message);
  }

  public static @NonNull ConfigException failedReadingConfig(@NonNull Path configFile,
      @NonNull Throwable cause) {
    String message = String.format(READ_CONFIG, configFile);
    return new ConfigException(message, cause);
  }

  public static @NonNull ConfigException resourceNotFound(@NonNull String configFileName) {
    String message = String.format(RESOURCE_NOT_FOUND, configFileName);
    return new ConfigException(message);
  }

  public static @NonNull ConfigException unexpectedEmptyDefaultConfig(
      @NonNull String configFileName) {
    String message = String.format(UNEXPECTED_EMPTY_DEFAULT_CONFIG, configFileName);
    return new ConfigException(message);
  }
}
