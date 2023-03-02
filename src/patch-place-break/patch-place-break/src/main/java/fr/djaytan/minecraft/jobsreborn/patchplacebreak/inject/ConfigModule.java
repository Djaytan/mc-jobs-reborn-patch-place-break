package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.ConfigApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import lombok.NonNull;

/** Represents config related configs (config of a config... You get it). */
public class ConfigModule extends AbstractModule {

  @Provides
  @Singleton
  public @NonNull DataSourceProperties provideDataSourceProperties(ConfigApi configApi) {
    return configApi.getDataSourceProperties();
  }
}
