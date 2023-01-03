package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import java.lang.reflect.Type;

import javax.inject.Singleton;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.HostProperties;

@Singleton
final class HostPropertiesSerializer implements TypeSerializer<HostProperties> {

  private static final String HOST_NAME = "hostname";
  private static final String PORT = "port";
  private static final String IS_SSL_ENABLED = "isSslEnabled";

  @Override
  public HostProperties deserialize(Type type, ConfigurationNode node)
      throws SerializationException {
    String hostName = node.node(HOST_NAME).require(String.class);
    int port = node.node(PORT).require(Integer.class);
    boolean isSslEnabled = node.node(IS_SSL_ENABLED).require(Boolean.class);
    return HostProperties.of(hostName, port, isSslEnabled);
  }

  @Override
  public void serialize(Type type, @Nullable HostProperties obj, ConfigurationNode node) {
    throw ConfigSerializationException.unexpectedSerialization();
  }
}
