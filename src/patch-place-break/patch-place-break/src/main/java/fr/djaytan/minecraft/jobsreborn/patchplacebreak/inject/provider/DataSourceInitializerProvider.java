package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.mysql.MysqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite.SqliteDataSourceInitializer;
import javax.inject.Inject;
import javax.inject.Provider;
import lombok.NonNull;

public class DataSourceInitializerProvider implements Provider<DataSourceInitializer> {

  private final DataSourceProperties dataSourceProperties;
  private final MysqlDataSourceInitializer mysqlDataSourceInitializer;
  private final SqliteDataSourceInitializer sqliteDataSourceInitializer;

  @Inject
  public DataSourceInitializerProvider(
      DataSourceProperties dataSourceProperties,
      MysqlDataSourceInitializer mysqlDataSourceInitializer,
      SqliteDataSourceInitializer sqliteDataSourceInitializer) {
    this.dataSourceProperties = dataSourceProperties;
    this.mysqlDataSourceInitializer = mysqlDataSourceInitializer;
    this.sqliteDataSourceInitializer = sqliteDataSourceInitializer;
  }

  @Override
  public @NonNull DataSourceInitializer get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case MYSQL:
        {
          return mysqlDataSourceInitializer;
        }
      case SQLITE:
        {
          return sqliteDataSourceInitializer;
        }
      default:
        {
          throw PatchPlaceBreakException.unsupportedDataSourceType(dataSourceType);
        }
    }
  }
}
