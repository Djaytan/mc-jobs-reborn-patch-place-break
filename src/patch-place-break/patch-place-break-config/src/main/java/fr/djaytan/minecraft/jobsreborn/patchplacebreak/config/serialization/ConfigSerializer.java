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
  public void serialize(@NonNull Path destConfigFile, @NonNull Object object) {
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
  public <T> @NonNull Optional<T> deserialize(@NonNull Path srcConfigFile, @NonNull Class<T> type) {
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
