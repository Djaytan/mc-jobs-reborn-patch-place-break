package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import java.lang.reflect.Type;

import javax.inject.Singleton;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;

@Singleton
final class DataSourcePropertiesSerializer implements TypeSerializer<DataSourceProperties> {

  private static final String TYPE = "type";
  private static final String TABLE = "table";
  private static final String DBMS_SERVER = "dbmsServer";

  @Override
  public DataSourceProperties deserialize(Type type, ConfigurationNode node)
      throws SerializationException {
    DataSourceType dataSourceType = node.node(TYPE).require(DataSourceType.class);
    String table = node.node(TABLE).require(String.class);
    DbmsServerProperties dbmsServer = node.node(DBMS_SERVER).require(DbmsServerProperties.class);
    return DataSourceProperties.of(dataSourceType, table, dbmsServer);
  }

  @Override
  public void serialize(Type type, @Nullable DataSourceProperties obj, ConfigurationNode node) {
    throw ConfigSerializationException.unexpectedSerialization();
  }
}
