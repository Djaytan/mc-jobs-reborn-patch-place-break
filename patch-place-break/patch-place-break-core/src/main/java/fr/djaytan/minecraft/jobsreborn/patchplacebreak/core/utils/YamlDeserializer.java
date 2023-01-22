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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.utils;

import java.nio.file.Path;
import java.util.Optional;

import javax.inject.Singleton;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.util.NamingSchemes;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import lombok.NonNull;

/**
 * YAML deserializer.
 */
@Singleton
public final class YamlDeserializer {

  /**
   * Deserializes the given YAML file into the specified type.
   *
   * @param yamlFile The YAML content to deserialize.
   * @param type The expected type of the YAML content.
   * @return The deserialized value if present and of valid type.
   * @param <T> The expected type to be obtained from deserialization.
   * @throws ConfigurateException If something prevent the deserialization.
   */
  public <T> @NonNull Optional<T> deserialize(@NonNull Path yamlFile, @NonNull Class<T> type)
      throws ConfigurateException {
    ConfigurationNode rootNode = buildYamlConfigurationNode(yamlFile);
    return Optional.ofNullable(rootNode.get(type));
  }

  private static @NonNull ConfigurationNode buildYamlConfigurationNode(@NonNull Path yamlFile)
      throws ConfigurateException {
    ObjectMapper.Factory customFactory =
        ObjectMapper.factoryBuilder().defaultNamingScheme(NamingSchemes.CAMEL_CASE).build();

    return YamlConfigurationLoader.builder().path(yamlFile)
        .defaultOptions(
            opts -> opts.serializers(builder -> builder.registerAnnotatedObjects(customFactory)))
        .build().load();
  }
}
