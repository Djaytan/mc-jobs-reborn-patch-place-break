package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.ConnectionPool;
import lombok.NonNull;

@Singleton
public class MysqlConnectionPool extends ConnectionPool {

  private static final String SERVER_TIME_ZONE = "UTC";

  @Inject
  MysqlConnectionPool(DataSourceProperties dataSourceProperties) {
    super(dataSourceProperties);
  }

  private static final String MYSQL_JDBC_URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=%s";

  @Override
  protected @NonNull String getJdbcUrl() {
    String hostName = dataSourceProperties.getHost().getHostName();
    int port = dataSourceProperties.getHost().getPort();
    String database = dataSourceProperties.getDatabaseName();
    boolean isSslEnabled = dataSourceProperties.getHost().isSslEnabled();
    return String.format(MYSQL_JDBC_URL_TEMPLATE, hostName, port, database, isSslEnabled,
        SERVER_TIME_ZONE);
  }
}
