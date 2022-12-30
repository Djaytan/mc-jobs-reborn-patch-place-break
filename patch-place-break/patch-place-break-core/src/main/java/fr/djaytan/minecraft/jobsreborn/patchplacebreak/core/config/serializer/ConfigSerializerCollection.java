package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.Config;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceCredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import lombok.NonNull;

@Singleton
public class ConfigSerializerCollection {

  private final ConfigSerializer configSerializer;
  private final DataSourcePropertiesSerializer dataSourcePropertiesSerializer;
  private final DataSourceHostPropertiesSerializer dataSourceHostPropertiesSerializer;
  private final DataSourceCredentialsPropertiesSerializer dataSourceCredentialsPropertiesSerializer;

  @Inject
  ConfigSerializerCollection(ConfigSerializer configSerializer,
      DataSourcePropertiesSerializer dataSourcePropertiesSerializer,
      DataSourceHostPropertiesSerializer dataSourceHostPropertiesSerializer,
      DataSourceCredentialsPropertiesSerializer dataSourceCredentialsPropertiesSerializer) {
    this.configSerializer = configSerializer;
    this.dataSourcePropertiesSerializer = dataSourcePropertiesSerializer;
    this.dataSourceHostPropertiesSerializer = dataSourceHostPropertiesSerializer;
    this.dataSourceCredentialsPropertiesSerializer = dataSourceCredentialsPropertiesSerializer;
  }

  public @NonNull TypeSerializerCollection collection() {
    return TypeSerializerCollection.builder().registerExact(Config.class, configSerializer)
        .registerExact(DataSourceProperties.class, dataSourcePropertiesSerializer)
        .registerExact(DataSourceHostProperties.class, dataSourceHostPropertiesSerializer)
        .registerExact(DataSourceCredentialsProperties.class,
            dataSourceCredentialsPropertiesSerializer)
        .build();
  }
}
