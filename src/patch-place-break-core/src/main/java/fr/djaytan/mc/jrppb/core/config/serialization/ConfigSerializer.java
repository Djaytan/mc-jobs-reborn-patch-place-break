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
package fr.djaytan.mc.jrppb.core.config.serialization;

import fr.djaytan.mc.jrppb.core.config.properties.Properties;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

/**
 * Config serializer.
 *
 * <p>Two methods are exposed for serialization: {@link #serialize(Path, Properties)} and {@link
 * #deserialize(Path, Class)}.
 */
@Singleton
public final class ConfigSerializer {

  /**
   * Serializes the given properties into the specified destination file.
   *
   * @param destConfigFile The destination config file into which serialize properties.
   * @param properties The properties to serialize.
   */
  public void serialize(@NotNull Path destConfigFile, @NotNull Properties properties) {
    try {
      ConfigurationLoader<? extends ConfigurationNode> serializer =
          createSerializer(destConfigFile);
      ConfigurationNode configurationNode = serializer.createNode(node -> node.set(properties));
      serializer.save(configurationNode);
    } catch (IOException e) {
      throw new UncheckedIOException(
          String.format(
              "Fail to serialize config properties of type '%s' in '%s' file",
              properties.getClass().getTypeName(), destConfigFile),
          e);
    }
  }

  private @NotNull ConfigurationLoader<? extends ConfigurationNode> createSerializer(
      @NotNull Path destConfigFile) throws IOException {
    ConfigurationLoader<? extends ConfigurationNode> loader =
        ConfigLoaderFactory.createLoader(destConfigFile);

    // TODO: really useful? And what about tests?
    if (!loader.canSave()) {
      throw new IllegalStateException(
          "The loader configuration is invalid and thus prevent processing serialization "
              + "of config files");
    }

    return loader;
  }

  /**
   * Deserializes the given source file to the specified targeted properties type.
   *
   * @param srcConfigFile The source config file from which deserializing.
   * @param propertiesType The targeted properties type of YAML deserialization.
   * @return The deserialized value if present and of valid properties type.
   * @param <T> The expected properties type to be obtained from deserialization.
   */
  public <T extends Properties> @NotNull Optional<T> deserialize(
      @NotNull Path srcConfigFile, @NotNull Class<T> propertiesType) {
    try {
      ConfigurationLoader<?> deserializer = createDeserializer(srcConfigFile);
      ConfigurationNode rootNode = deserializer.load();
      return Optional.ofNullable(rootNode.get(propertiesType));
    } catch (IOException e) {
      throw new UncheckedIOException(
          String.format(
              "Fail to deserialize config properties of type '%s' from '%s' file",
              propertiesType.getTypeName(), srcConfigFile),
          e);
    }
  }

  private @NotNull ConfigurationLoader<? extends ConfigurationNode> createDeserializer(
      @NotNull Path srcConfigFile) throws IOException {
    ConfigurationLoader<? extends ConfigurationNode> loader =
        ConfigLoaderFactory.createLoader(srcConfigFile);

    // TODO: really useful? And what about tests?
    if (!loader.canLoad()) {
      throw new IllegalStateException(
          "The loader configuration is invalid and thus prevent processing deserialization "
              + "of config files");
    }

    return loader;
  }
}
