package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql;

import static com.google.common.base.Preconditions.checkState;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.StorageException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.SqlHelper;

@Singleton
public class MysqlDataSourceInitializer extends SqlDataSourceInitializer {

  private final DataSourceProperties dataSourceProperties;

  @Inject
  MysqlDataSourceInitializer(DataSourceProperties dataSourceProperties, SqlHelper sqlHelper) {
    super(sqlHelper);
    this.dataSourceProperties = dataSourceProperties;
  }

  @Override
  public void initialize() throws StorageException {
    checkState(dataSourceProperties.getType() == DataSourceType.MYSQL,
        "The data source type is expected to be 'MYSQL'.");
  }
}
