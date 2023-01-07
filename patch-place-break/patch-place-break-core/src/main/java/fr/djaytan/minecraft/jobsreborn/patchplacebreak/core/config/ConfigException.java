/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
