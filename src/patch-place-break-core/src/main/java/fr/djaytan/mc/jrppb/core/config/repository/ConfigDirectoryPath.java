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
package fr.djaytan.mc.jrppb.core.config.repository;

import fr.djaytan.mc.jrppb.core.config.ConfigName;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

/** Represents a config directory path. */
public record ConfigDirectoryPath(@NotNull Path value) {

  public ConfigDirectoryPath(@NotNull Path value) {
    this.value = value.toAbsolutePath().normalize();
  }

  /**
   * Returns the config directory path.
   *
   * <p>Note that the path is guaranteed to be absolute and normalized.
   *
   * @return The config directory path.
   */
  public @NotNull Path value() {
    return value;
  }

  /**
   * Resolves the config file path from this directory based on the provided config name.
   *
   * <p>Note that the returned path is guaranteed to be absolute and normalized.
   *
   * @param configName The config name used to resolve the config file path.
   * @return The resolved config file path from this directory.
   */
  public @NotNull Path resolveConfigFilePath(@NotNull ConfigName configName) {
    return value.resolve(configName.value() + ".conf");
  }
}
