package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.Config;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.HostProperties;
import lombok.NonNull;

@Singleton
public class ConfigSerializerCollection {

  private final ConfigSerializer configSerializer;
  private final ConnectionPoolPropertiesSerializer connectionPoolPropertiesSerializer;
  private final DataSourcePropertiesSerializer dataSourcePropertiesSerializer;
  private final DbmsServerPropertiesSerializer dbmsServerPropertiesSerializer;
  private final HostPropertiesSerializer hostPropertiesSerializer;
  private final CredentialsPropertiesSerializer credentialsPropertiesSerializer;

  @Inject
  ConfigSerializerCollection(ConfigSerializer configSerializer,
      ConnectionPoolPropertiesSerializer connectionPoolPropertiesSerializer,
      DataSourcePropertiesSerializer dataSourcePropertiesSerializer,
      DbmsServerPropertiesSerializer dbmsServerPropertiesSerializer,
      HostPropertiesSerializer hostPropertiesSerializer,
      CredentialsPropertiesSerializer credentialsPropertiesSerializer) {
    this.configSerializer = configSerializer;
    this.connectionPoolPropertiesSerializer = connectionPoolPropertiesSerializer;
    this.dataSourcePropertiesSerializer = dataSourcePropertiesSerializer;
    this.dbmsServerPropertiesSerializer = dbmsServerPropertiesSerializer;
    this.hostPropertiesSerializer = hostPropertiesSerializer;
    this.credentialsPropertiesSerializer = credentialsPropertiesSerializer;
  }

  public @NonNull TypeSerializerCollection collection() {
    return TypeSerializerCollection.builder().registerExact(Config.class, configSerializer)
        .registerExact(DataSourceProperties.class, dataSourcePropertiesSerializer)
        .registerExact(DbmsServerProperties.class, dbmsServerPropertiesSerializer)
        .registerExact(ConnectionPoolProperties.class, connectionPoolPropertiesSerializer)
        .registerExact(HostProperties.class, hostPropertiesSerializer)
        .registerExact(CredentialsProperties.class, credentialsPropertiesSerializer).build();
  }
}
