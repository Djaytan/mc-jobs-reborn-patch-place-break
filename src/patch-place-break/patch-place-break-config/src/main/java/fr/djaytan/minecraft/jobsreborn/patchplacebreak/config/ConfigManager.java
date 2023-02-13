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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidator;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.ValidatingConvertibleProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Manages config creation and manipulations.
 *
 * <p>The method {@link #createDefaultIfNotExists(String, ValidatingConvertibleProperties)}
 * permits to create a config if missing under the data folder.
 * Then, {@link #readAndValidate(String, Class)} can be used to read and validate it.
 *
 * <p>The data folder definition is let to the patch enabler (e.g. the Bukkit plugin enabling and
 * calling the patch).
 */
@Slf4j
@Singleton
final class ConfigManager {

  private final Path dataFolder;
  private final ConfigSerializer configSerializer;
  private final PropertiesValidator propertiesValidator;

  @Inject
  ConfigManager(@Named("dataFolder") Path dataFolder, ConfigSerializer configSerializer,
      PropertiesValidator propertiesValidator) {
    this.dataFolder = dataFolder;
    this.configSerializer = configSerializer;
    this.propertiesValidator = propertiesValidator;
  }

  /**
   * Creates config file with the given name if it doesn't exist yet.
   *
   * <p>The config file is created under the "dataFolder" path.
   *
   * @param configFileName The config file name to create if not exists yet.
   * @param defaultProperties The default properties to serialize
   *                          if the config file doesn't exist yet.
   */
  public void createDefaultIfNotExists(@NonNull String configFileName,
      @NonNull ValidatingConvertibleProperties<?> defaultProperties) throws ConfigException {
    Path configFile = dataFolder.resolve(configFileName);

    if (Files.exists(configFile)) {
      return;
    }

    log.atInfo().log("No config file detected: creating default one.");
    propertiesValidator.validate(defaultProperties);
    configSerializer.serialize(configFile, defaultProperties);
    log.atInfo().log("Default configuration file created.");
  }

  /**
   * Reads config file from a predefined location. It is recommended to call
   * {@link #createDefaultIfNotExists(String, ValidatingConvertibleProperties)} first at least one
   * time.
   *
   * @param configFileName The config file name to read under the data folder.
   * @param type The type to be instantiated and returned once config file read.
   * @param <T> The final type to be instantiated and returned once config file validated.
   * @return The specified final type instantiated and populated with values coming from
   * the config file content.
   */
  public <T> @NonNull T readAndValidate(@NonNull String configFileName,
      Class<? extends ValidatingConvertibleProperties<T>> type) throws ConfigException {
    ValidatingConvertibleProperties<T> configValidatingProperties =
        readAndDeserializeConfigToValidate(configFileName, type);
    return propertiesValidator.validate(configValidatingProperties);
  }

  private <T> @NonNull ValidatingConvertibleProperties<T> readAndDeserializeConfigToValidate(
      @NonNull String configFileName,
      @NonNull Class<? extends ValidatingConvertibleProperties<T>> type)
      throws ConfigSerializationException {
    Path configFile = dataFolder.resolve(configFileName);

    log.atInfo().log("Reading '{}' file...", configFileName);
    Optional<? extends ValidatingConvertibleProperties<T>> configValidatingProperties =
        configSerializer.deserialize(configFile, type);

    if (!configValidatingProperties.isPresent()) {
      throw ConfigException.failedReadingConfig(configFileName);
    }

    ValidatingConvertibleProperties<T> readConfig = configValidatingProperties.get();
    log.atInfo().log("File '{}' read successfully.", configFileName);
    return readConfig;
  }
}
