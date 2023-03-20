/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization;

import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.experimental.StandardException;
import org.jetbrains.annotations.NotNull;

@StandardException(access = AccessLevel.PROTECTED)
public class ConfigSerializationException extends RuntimeException {

  private static final String INVALID_LOADER_CONFIGURATION =
      "The loader configuration is invalid and thus prevent processing "
          + "serializations and deserializations of config files";
  private static final String FAIL_TO_SERIALIZE =
      "Fail to serialize config properties of type '%s' from '%s' file";
  private static final String FAIL_TO_DESERIALIZE =
      "Fail to deserialize config properties of type '%s' from '%s' file";

  public static @NotNull ConfigSerializationException serialization(
      @NotNull Path destConfigFile, @NotNull Class<?> propertiesType, @NotNull Throwable cause) {
    String message = String.format(FAIL_TO_SERIALIZE, propertiesType.getName(), destConfigFile);
    return new ConfigSerializationException(message, cause);
  }

  public static @NotNull ConfigSerializationException deserialization(
      @NotNull Path srcConfigFile, @NotNull Class<?> propertiesType, @NotNull Throwable cause) {
    String message = String.format(FAIL_TO_DESERIALIZE, propertiesType.getName(), srcConfigFile);
    return new ConfigSerializationException(message, cause);
  }

  public static @NotNull ConfigSerializationException invalidLoaderConfiguration() {
    return new ConfigSerializationException(INVALID_LOADER_CONFIGURATION);
  }
}
