package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakStandaloneException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql.MysqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sqlite.SqliteDataSourceInitializer;

public class SqlDataSourceInitializerProvider implements Provider<SqlDataSourceInitializer> {

  private final DataSourceProperties dataSourceProperties;
  private final MysqlDataSourceInitializer mysqlDataSourceInitializer;
  private final SqliteDataSourceInitializer sqliteDataSourceInitializer;

  @Inject
  SqlDataSourceInitializerProvider(DataSourceProperties dataSourceProperties,
      MysqlDataSourceInitializer mysqlDataSourceInitializer,
      SqliteDataSourceInitializer sqliteDataSourceInitializer) {
    this.dataSourceProperties = dataSourceProperties;
    this.mysqlDataSourceInitializer = mysqlDataSourceInitializer;
    this.sqliteDataSourceInitializer = sqliteDataSourceInitializer;
  }

  @Override
  public SqlDataSourceInitializer get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case MYSQL: {
        return mysqlDataSourceInitializer;
      }
      case SQLITE: {
        return sqliteDataSourceInitializer;
      }
      default: {
        throw PatchPlaceBreakStandaloneException.unsupportedDataSourceType(dataSourceType);
      }
    }
  }
}
