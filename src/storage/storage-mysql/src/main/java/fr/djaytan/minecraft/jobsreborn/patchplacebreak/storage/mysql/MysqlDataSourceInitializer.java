package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.mysql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.Validate;

/** Represents an initializer for a MySQL data source. */
@Singleton
public final class MysqlDataSourceInitializer implements DataSourceInitializer {

  private final DataSourceProperties dataSourceProperties;

  @Inject
  public MysqlDataSourceInitializer(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  /** {@inheritDoc} */
  @Override
  public void initialize() {
    Validate.validState(
        dataSourceProperties.getType() == DataSourceType.MYSQL,
        "The data source type is expected to be 'MYSQL'.");
  }
}
