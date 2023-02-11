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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization;

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
 * Config serializer.
 *
 * <p>For now, only deserialization is supported through {@link #deserialize(Path, Class)} method.
 * Due to an existing issue (https://github.com/SpongePowered/Configurate/issues/379) from the
 * Configurate library the serialization for YAML, serialization is not possible yet unless using
 * alternative.
 */
@Singleton
public final class ConfigSerializer {

  /**
   * Deserializes the given source file to the specified targeted type.
   *
   * @param srcFile The source file from which deserializing.
   * @param type The targeted type of YAML deserialization.
   * @return The deserialized value if present and of valid type.
   * @param <T> The expected type to be obtained from deserialization.
   * @throws ConfigSerializationException If something prevent the deserialization.
   */
  public <T> @NonNull Optional<T> deserialize(@NonNull Path srcFile, @NonNull Class<T> type)
      throws ConfigSerializationException {
    YamlConfigurationLoader loader = createYamlLoader(srcFile);

    if (!loader.canLoad()) {
      throw ConfigSerializationException.failToDeserialize();
    }

    try {
      ConfigurationNode rootNode = loader.load();
      return Optional.ofNullable(rootNode.get(type));
    } catch (ConfigurateException e) {
      throw ConfigSerializationException.failToDeserialize(e);
    }
  }

  private static @NonNull YamlConfigurationLoader createYamlLoader(@NonNull Path yamlFile) {
    ObjectMapper.Factory customFactory =
        ObjectMapper.factoryBuilder().defaultNamingScheme(NamingSchemes.CAMEL_CASE).build();

    return YamlConfigurationLoader.builder().path(yamlFile)
        .defaultOptions(
            opts -> opts.serializers(builder -> builder.registerAnnotatedObjects(customFactory)))
        .build();
  }
}
