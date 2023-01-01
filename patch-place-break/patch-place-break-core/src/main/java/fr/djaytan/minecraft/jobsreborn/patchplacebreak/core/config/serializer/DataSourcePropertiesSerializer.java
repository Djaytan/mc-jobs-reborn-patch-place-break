package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceCredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;

final class DataSourcePropertiesSerializer implements TypeSerializer<DataSourceProperties> {

  private static final String TYPE = "type";
  private static final String HOST = "host";
  private static final String CREDENTIALS = "credentials";
  private static final String DATABASE_NAME = "databaseName";
  private static final String TABLE_NAME = "tableName";

  @Override
  public DataSourceProperties deserialize(Type type, ConfigurationNode node)
      throws SerializationException {
    DataSourceType dataSourceType = node.node(TYPE).require(DataSourceType.class);
    DataSourceHostProperties host = node.node(HOST).require(DataSourceHostProperties.class);
    DataSourceCredentialsProperties credentials =
        node.node(CREDENTIALS).require(DataSourceCredentialsProperties.class);
    String databaseName = node.node(DATABASE_NAME).require(String.class);
    String tableName = node.node(TABLE_NAME).require(String.class);
    return DataSourceProperties.of(dataSourceType, host, credentials, databaseName, tableName);
  }

  @Override
  public void serialize(Type type, @Nullable DataSourceProperties obj, ConfigurationNode node) {
    throw ConfigSerializationException.unexpectedSerialization();
  }
}
