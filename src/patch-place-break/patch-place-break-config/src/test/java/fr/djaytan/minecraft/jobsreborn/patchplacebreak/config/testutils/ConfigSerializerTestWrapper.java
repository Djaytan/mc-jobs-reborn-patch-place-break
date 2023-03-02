package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializer;
import java.nio.file.Path;
import java.util.Optional;
import lombok.NonNull;

/** Wrapper class for config serialization in tests. */
public final class ConfigSerializerTestWrapper {

  /**
   * @see ConfigSerializer#serialize(Path, Object)
   */
  public static void serialize(@NonNull Path destConfigFile, @NonNull Object object) {
    ConfigSerializer configSerializer = new ConfigSerializer();
    configSerializer.serialize(destConfigFile, object);
  }

  /**
   * @see ConfigSerializer#deserialize(Path, Class)
   */
  public static <T> @NonNull Optional<T> deserialize(
      @NonNull Path srcConfigFile, @NonNull Class<T> type) {
    ConfigSerializer configSerializer = new ConfigSerializer();
    return configSerializer.deserialize(srcConfigFile, type);
  }
}
