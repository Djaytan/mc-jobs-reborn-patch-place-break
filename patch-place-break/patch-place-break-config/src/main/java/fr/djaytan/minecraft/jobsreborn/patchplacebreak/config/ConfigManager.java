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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.ConfigValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.deserialization.YamlDeserializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.deserialization.YamlDeserializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Manages config creation and manipulations.
 *
 * <p>The method {@link #createIfNotExists()} permits to create the config if missing at
 * a predefined location. Then, {@link #readAndValidate()} can be used to read
 * and validate it.
 *
 * <p>The predefined location definition responsibility is let to the patch enabler (e.g. the
 * Bukkit plugin enabling and calling the patch).
 */
@Slf4j
@Singleton
public class ConfigManager {

  public static final String CONFIG_FILE_NAME = "config.yml";

  private final ClassLoader classLoader;
  private final Path configFile;
  private final PropertiesValidator propertiesValidator;
  private final YamlDeserializer yamlDeserializer;

  @Inject
  ConfigManager(ClassLoader classLoader, @Named("configFile") Path configFile,
      PropertiesValidator propertiesValidator, YamlDeserializer yamlDeserializer) {
    this.classLoader = classLoader;
    this.configFile = configFile;
    this.propertiesValidator = propertiesValidator;
    this.yamlDeserializer = yamlDeserializer;
  }

  /**
   * Creates default config file if it doesn't exist yet to a predefined location.
   *
   * @throws ConfigException If the default file to be copied can't be found
   *    or something else prevent the default config file to be copied successfully.
   */
  public void createIfNotExists() throws ConfigException {
    if (Files.exists(configFile)) {
      return;
    }
    log.atInfo().log("No config file detected: creating default one.");
    create();
  }

  private void create() {
    try (InputStream configFileIs = classLoader.getResourceAsStream(CONFIG_FILE_NAME)) {
      if (configFileIs == null) {
        throw ConfigException.resourceNotFound(CONFIG_FILE_NAME);
      }
      copyDefault(configFileIs);
      log.atInfo().log("Default configuration file created.");
    } catch (IOException e) {
      throw ConfigException.failedCreatingDefault(CONFIG_FILE_NAME, e);
    }
  }

  private void copyDefault(@NonNull InputStream configFileIs) throws IOException {
    long configFileSize = Files.copy(configFileIs, configFile);

    if (configFileSize <= 0) {
      throw ConfigException.unexpectedEmptyDefaultConfig(CONFIG_FILE_NAME);
    }
  }

  /**
   * Reads config file from a predefined location. It is recommended to call
   * {@link #createIfNotExists()} first at least one time.
   *
   * @return The validated config properties.
   * @throws ConfigException If the config file doesn't exist at the predefined location
   *    or something else prevent config file reading or validation.
   * @throws PropertiesValidationException If read config isn't valid.
   */
  public @NonNull ConfigProperties readAndValidate() throws PropertiesValidationException {
    ConfigValidatingProperties configValidatingProperties = readAndDeserializeConfigToValidate();
    return propertiesValidator.validate(configValidatingProperties);
  }

  private @NonNull ConfigValidatingProperties readAndDeserializeConfigToValidate() {
    try {
      log.atInfo().log("Reading '{}' file...", CONFIG_FILE_NAME);
      Optional<ConfigValidatingProperties> configValidatingProperties =
          yamlDeserializer.deserialize(configFile, ConfigValidatingProperties.class);

      if (!configValidatingProperties.isPresent()) {
        throw ConfigException.failedReadingConfig(configFile);
      }

      ConfigValidatingProperties readConfig = configValidatingProperties.get();
      log.atInfo().log("File '{}' read successfully.", CONFIG_FILE_NAME);
      return readConfig;
    } catch (YamlDeserializationException e) {
      throw ConfigException.failedReadingConfig(configFile, e);
    }
  }
}
