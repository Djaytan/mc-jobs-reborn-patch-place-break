package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakStandaloneException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql.MysqlConnectionPool;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.ConnectionPool;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sqlite.SqliteConnectionPool;
import lombok.NonNull;

public class ConnectionPoolProvider implements Provider<ConnectionPool> {

  private final DataSourceProperties dataSourceProperties;
  private final MysqlConnectionPool mysqlConnectionPool;
  private final SqliteConnectionPool sqliteConnectionPool;

  @Inject
  ConnectionPoolProvider(@NonNull DataSourceProperties dataSourceProperties,
      @NonNull MysqlConnectionPool mysqlConnectionPool,
      @NonNull SqliteConnectionPool sqliteConnectionPool) {
    this.dataSourceProperties = dataSourceProperties;
    this.mysqlConnectionPool = mysqlConnectionPool;
    this.sqliteConnectionPool = sqliteConnectionPool;
  }

  @Override
  public ConnectionPool get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case MYSQL: {
        return mysqlConnectionPool;
      }
      case SQLITE: {
        return sqliteConnectionPool;
      }
      default: {
        throw PatchPlaceBreakStandaloneException.unsupportedDataSourceType(dataSourceType);
      }
    }
  }
}
