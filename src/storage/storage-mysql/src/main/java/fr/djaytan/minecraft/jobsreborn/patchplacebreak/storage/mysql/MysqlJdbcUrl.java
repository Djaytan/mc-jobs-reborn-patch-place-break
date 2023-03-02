package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.mysql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsServerProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public final class MysqlJdbcUrl implements JdbcUrl {

  private static final String MYSQL_JDBC_URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=%s";
  private static final String SERVER_TIME_ZONE = "UTC";

  private final DataSourceProperties dataSourceProperties;

  @Inject
  public MysqlJdbcUrl(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  @Override
  public @NonNull String get() {
    DbmsServerProperties dbmsServerProperties = dataSourceProperties.getDbmsServer();
    String hostname = dbmsServerProperties.getHost().getHostname();
    int port = dbmsServerProperties.getHost().getPort();
    String database = dbmsServerProperties.getDatabase();
    boolean isSslEnabled = dbmsServerProperties.getHost().isSslEnabled();
    return String.format(
        MYSQL_JDBC_URL_TEMPLATE, hostname, port, database, isSslEnabled, SERVER_TIME_ZONE);
  }
}
