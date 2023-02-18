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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javax.inject.Singleton;
import lombok.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

/**
 * Config serializer.
 *
 * <p>Two methods are exposed for serialization: {@link #serialize(Path, Object)} and {@link
 * #deserialize(Path, Class)}.
 */
@Singleton
public final class ConfigSerializer {

  /**
   * Serializes the given object into the specified destination file.
   *
   * @param destConfigFile The destination config file into which serialize object.
   * @param object The object to serialize.
   * @throws ConfigSerializationException If something prevent the serialization.
   */
  public void serialize(@NonNull Path destConfigFile, @NonNull Object object)
      throws ConfigSerializationException {
    try {
      ConfigurationLoader<? extends ConfigurationNode> loader =
          ConfigLoaderFactory.createLoader(destConfigFile);

      if (!loader.canSave()) {
        throw ConfigSerializationException.failToSerialize();
      }

      ConfigurationNode configurationNode = loader.createNode(node -> node.set(object));
      loader.save(configurationNode);
    } catch (IOException e) {
      throw ConfigSerializationException.failToSerialize(e);
    }
  }

  /**
   * Deserializes the given source file to the specified targeted type.
   *
   * @param srcConfigFile The source config file from which deserializing.
   * @param type The targeted type of YAML deserialization.
   * @return The deserialized value if present and of valid type.
   * @param <T> The expected type to be obtained from deserialization.
   * @throws ConfigSerializationException If something prevent the deserialization.
   */
  public <T> @NonNull Optional<T> deserialize(@NonNull Path srcConfigFile, @NonNull Class<T> type)
      throws ConfigSerializationException {
    try {
      ConfigurationLoader<?> loader = ConfigLoaderFactory.createLoader(srcConfigFile);

      if (!loader.canLoad()) {
        throw ConfigSerializationException.failToDeserialize();
      }

      ConfigurationNode rootNode = loader.load();
      return Optional.ofNullable(rootNode.get(type));
    } catch (IOException e) {
      throw ConfigSerializationException.failToDeserialize(e);
    }
  }
}
