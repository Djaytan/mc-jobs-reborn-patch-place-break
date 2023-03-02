package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.mysql.MysqlJdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite.SqliteJdbcUrl;
import javax.inject.Inject;
import javax.inject.Provider;
import lombok.NonNull;

public class JdbcUrlProvider implements Provider<JdbcUrl> {

  private final DataSourceProperties dataSourceProperties;
  private final MysqlJdbcUrl mysqlJdbcUrl;
  private final SqliteJdbcUrl sqliteJdbcUrl;

  @Inject
  public JdbcUrlProvider(
      DataSourceProperties dataSourceProperties,
      MysqlJdbcUrl mysqlJdbcUrl,
      SqliteJdbcUrl sqliteJdbcUrl) {
    this.dataSourceProperties = dataSourceProperties;
    this.mysqlJdbcUrl = mysqlJdbcUrl;
    this.sqliteJdbcUrl = sqliteJdbcUrl;
  }

  @Override
  public @NonNull JdbcUrl get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case MYSQL:
        {
          return mysqlJdbcUrl;
        }
      case SQLITE:
        {
          return sqliteJdbcUrl;
        }
      default:
        {
          throw PatchPlaceBreakException.unsupportedDataSourceType(dataSourceType);
        }
    }
  }
}
