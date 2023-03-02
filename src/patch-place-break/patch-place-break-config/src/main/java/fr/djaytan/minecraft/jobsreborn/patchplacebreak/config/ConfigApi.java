package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DataSourceValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;

/** API permitting to retrieve config properties. */
@Singleton
public final class ConfigApi {

  private static final String DATA_SOURCE_CONFIG_FILE_NAME = "dataSource.conf";

  private final ConfigManager configManager;

  @Inject
  public ConfigApi(ConfigManager configManager) {
    this.configManager = configManager;
  }

  /**
   * Retrieves data source related properties.
   *
   * @return The data source related properties.
   */
  public @NonNull DataSourceProperties getDataSourceProperties() {
    configManager.createDefaultIfNotExists(
        DATA_SOURCE_CONFIG_FILE_NAME, DataSourceValidatingProperties.ofDefault());
    return configManager.readAndValidate(
        DATA_SOURCE_CONFIG_FILE_NAME, DataSourceValidatingProperties.class);
  }
}
