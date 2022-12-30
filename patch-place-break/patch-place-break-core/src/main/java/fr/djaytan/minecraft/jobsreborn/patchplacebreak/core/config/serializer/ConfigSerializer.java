package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.Config;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import java.lang.reflect.Type;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class ConfigSerializer implements TypeSerializer<Config> {

  private static final String DATASOURCE = "dataSource";

  @Override
  public Config deserialize(Type type, ConfigurationNode node) throws SerializationException {
    DataSourceProperties dataSourceProperties =
        node.node(DATASOURCE).require(DataSourceProperties.class);
    return Config.of(dataSourceProperties);
  }

  @Override
  public void serialize(Type type, @Nullable Config obj, ConfigurationNode node) {
    throw ConfigSerializationException.unexpectedSerialization();
  }
}
