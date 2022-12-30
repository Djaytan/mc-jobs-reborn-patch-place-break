package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceHostProperties;
import java.lang.reflect.Type;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

final class DataSourceHostPropertiesSerializer implements TypeSerializer<DataSourceHostProperties> {

  private static final String HOST_NAME = "hostName";
  private static final String PORT = "port";
  private static final String IS_SSL_ENABLED = "isSslEnabled";

  @Override
  public DataSourceHostProperties deserialize(Type type, ConfigurationNode node)
      throws SerializationException {
    String hostName = node.node(HOST_NAME).require(String.class);
    int port = node.node(PORT).require(Integer.class);
    boolean isSslEnabled = node.node(IS_SSL_ENABLED).require(Boolean.class);
    return DataSourceHostProperties.of(hostName, port, isSslEnabled);
  }

  @Override
  public void serialize(Type type, @Nullable DataSourceHostProperties obj, ConfigurationNode node) {
    throw ConfigSerializationException.unexpectedSerialization();
  }
}
