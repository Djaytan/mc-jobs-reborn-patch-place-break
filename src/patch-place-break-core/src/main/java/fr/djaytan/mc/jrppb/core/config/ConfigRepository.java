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
package fr.djaytan.mc.jrppb.core.config;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class ConfigRepository {

  private final ConfigDirectoryPath configDirectoryPath;

  @Inject
  public ConfigRepository(@NotNull ConfigDirectoryPath configDirectoryPath) {
    this.configDirectoryPath = configDirectoryPath;
  }

  /**
   * Creates a new config.
   *
   * <p>It is recommended to first check for config existence with {@link #exists(ConfigName)}
   * before calling this method.
   *
   * @param configName The name of the config to create.
   * @param content The content of the config to create.
   * @throws IllegalStateException If the config already exists.
   */
  public void create(@NotNull ConfigName configName, @NotNull String content) {
    createConfigDirectoryIfNotExists();
    createNewConfigFile(configName, content);
  }

  private void createConfigDirectoryIfNotExists() {
    try {
      Files.createDirectories(configDirectoryPath.value());
    } catch (IOException e) {
      throw new UncheckedIOException(
          "Failed to create (if not already exists) the directory '%s'"
              .formatted(configDirectoryPath.value()),
          e);
    }
  }

  private void createNewConfigFile(@NotNull ConfigName configName, @NotNull String content) {
    Path configFile = configDirectoryPath.resolveConfigFilePath(configName);

    try {
      Files.writeString(configFile, content, CREATE_NEW, WRITE);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to create config file '%s'".formatted(configFile), e);
    }
  }

  /**
   * Checks config existence.
   *
   * @param configName The name of the config to check existence.
   * @return {@code true} if the config exists, {@code false} otherwise.
   */
  public boolean exists(@NotNull ConfigName configName) {
    return findByName(configName).isPresent();
  }

  /**
   * Searches the config matching the provided name if it exists.
   *
   * @param configName The name of the config to search.
   * @return The matching config if it exists, otherwise nothing.
   */
  public @NotNull Optional<String> findByName(@NotNull ConfigName configName) {
    Path configFile = configDirectoryPath.resolveConfigFilePath(configName);

    if (Files.notExists(configFile)) {
      return Optional.empty();
    }

    try {
      return Optional.of(Files.readString(configFile));
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read config file '%s'".formatted(configFile), e);
    }
  }
}
