package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.ConnectionPool;
import lombok.NonNull;

@Singleton
public class MysqlConnectionPool extends ConnectionPool {

  private final DataSourceProperties dataSourceProperties;

  @Inject
  MysqlConnectionPool(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  private static final String MYSQL_JDBC_URL_TEMPLATE =
      "jdbc:mysql://%s:%s@%s:%d?useSSL=%s&serverTimezone=UTC";

  @Override
  protected @NonNull String getJdbcUrl() {
    String username = dataSourceProperties.getCredentials().getUsername();
    String password = dataSourceProperties.getCredentials().getPassword();
    String hostName = dataSourceProperties.getHost().getHostName();
    int port = dataSourceProperties.getHost().getPort();
    boolean isSslEnabled = dataSourceProperties.getHost().isSslEnabled();
    return String.format(MYSQL_JDBC_URL_TEMPLATE, username, password, hostName, port, isSslEnabled);
  }
}
