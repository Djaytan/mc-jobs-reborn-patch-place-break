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
public class MysqlDataSourceInitializer implements SqlDataSourceInitializer {

  private final DataSourceProperties dataSourceProperties;
  private final SqlHelper sqlHelper;

  @Inject
  MysqlDataSourceInitializer(DataSourceProperties dataSourceProperties, SqlHelper sqlHelper) {
    this.dataSourceProperties = dataSourceProperties;
    this.sqlHelper = sqlHelper;
  }

  @Override
  public void initialize() throws StorageException {
    checkState(dataSourceProperties.getType() == DataSourceType.MYSQL,
        "The data source type is expected to be 'MYSQL'.");
  }

  @Override
  public void postConnection() throws StorageException {
    sqlHelper.createDatabaseIfNotExists();
    sqlHelper.switchToDatabase();
    sqlHelper.createTableIfNotExists();
  }
}
