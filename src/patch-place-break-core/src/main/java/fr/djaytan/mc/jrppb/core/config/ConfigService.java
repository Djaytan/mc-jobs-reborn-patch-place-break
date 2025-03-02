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

import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.deserialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.serialize;

import fr.djaytan.mc.jrppb.core.config.properties_v2.ConfigProperties;
import fr.djaytan.mc.jrppb.core.config.repository.ConfigRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class ConfigService {

  private static final Logger log = LoggerFactory.getLogger(ConfigService.class);

  private final ConfigRepository configRepository;

  @Inject
  public ConfigService(@NotNull ConfigRepository configRepository) {
    this.configRepository = configRepository;
  }

  /**
   * Creates the config based on the provided properties if it doesn't exist yet.
   *
   * @param configName The name of the config to eventually create.
   * @param configProperties The properties of the config to eventually create.
   */
  public void createIfNotExists(
      @NotNull ConfigName configName, @NotNull ConfigProperties configProperties) {
    if (configRepository.exists(configName)) {
      return;
    }

    configRepository.create(configName, serialize(configProperties));
    log.info("New config file created because it didn't exist yet: {}", configName);
  }

  /**
   * Reads the config matching the provided name and returns a value mapped to the specified
   * properties type.
   *
   * @param configName The name of the config to search.
   * @param configPropertiesType The wanted type of the config properties to be mapped on prior to
   *     return them.
   * @return The config properties of the wanted type.
   * @param <T> The type of the config properties.
   * @throws IllegalStateException If the config can't be found whereas it shall exist.
   */
  public <T extends ConfigProperties> @NotNull T load(
      @NotNull ConfigName configName, @NotNull Class<T> configPropertiesType) {
    Optional<String> serializedConfigProperties = configRepository.findByName(configName);

    if (serializedConfigProperties.isEmpty()) {
      throw new IllegalStateException(
          "The config '%s' can't be found whereas it shall exist".formatted(configName));
    }

    return deserialize(serializedConfigProperties.get(), configPropertiesType);
  }
}
