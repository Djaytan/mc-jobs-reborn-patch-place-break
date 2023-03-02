package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataMigrationExecutor;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class SqlDataSourceManager implements DataSourceManager {

  private final ConnectionPool connectionPool;
  private final DataMigrationExecutor dataMigrationExecutor;
  private final DataSourceInitializer dataSourceInitializer;

  @Inject
  public SqlDataSourceManager(
      ConnectionPool connectionPool,
      DataMigrationExecutor dataMigrationExecutor,
      DataSourceInitializer dataSourceInitializer) {
    this.connectionPool = connectionPool;
    this.dataMigrationExecutor = dataMigrationExecutor;
    this.dataSourceInitializer = dataSourceInitializer;
  }

  @Override
  public void connect() {
    dataSourceInitializer.initialize();

    connectionPool.connect();
    DataSource dataSource = connectionPool.getDataSource();

    dataMigrationExecutor.migrate(dataSource);
  }

  @Override
  public void disconnect() {
    connectionPool.disconnect();
  }
}
