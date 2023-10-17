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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties.Properties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Manages config creation and manipulations.
 *
 * <p>The method {@link #createDefaultIfNotExists(String, Properties)} permits to create a config if
 * missing under the data folder. Then, {@link #readAndValidate(String, Class)} can be used to read
 * and validate it.
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
  public ConfigManager(
      @NotNull @Named("dataFolder") Path dataFolder,
      @NotNull ConfigSerializer configSerializer,
      @NotNull PropertiesValidator propertiesValidator) {
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
   * @param defaultProperties The default properties to serialize if the config file doesn't exist
   *     yet.
   */
  public void createDefaultIfNotExists(
      @NotNull String configFileName, @NotNull Properties defaultProperties) {
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
   * Reads config file from a predefined location. It is recommended to call {@link
   * #createDefaultIfNotExists(String, Properties)} first at least one time.
   *
   * @param configFileName The config file name to read under the data folder.
   * @param propertiesType The type to be instantiated and returned once config file read.
   * @param <T> The final type to be instantiated and returned once config file validated.
   * @return The specified final type instantiated and populated with values coming from the config
   *     file content.
   */
  public <T extends Properties> @NotNull T readAndValidate(
      @NotNull String configFileName, @NotNull Class<T> propertiesType) {
    T properties = readAndDeserializeProperties(configFileName, propertiesType);
    propertiesValidator.validate(properties);
    return properties;
  }

  private <T extends Properties> @NotNull T readAndDeserializeProperties(
      @NotNull String configFileName, @NotNull Class<T> propertiesType) {
    Path configFile = dataFolder.resolve(configFileName);

    log.atInfo().log("Reading '{}' file...", configFileName);
    Optional<T> properties = configSerializer.deserialize(configFile, propertiesType);

    if (!properties.isPresent()) {
      throw ConfigException.failedReadingConfig(configFileName);
    }

    log.atInfo().log("File '{}' read successfully.", configFileName);
    return properties.get();
  }
}
