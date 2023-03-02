package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class ConnectionPool {

  private final DataSourceProperties dataSourceProperties;
  private final JdbcUrl jdbcUrl;
  private HikariDataSource hikariDataSource;

  @Inject
  public ConnectionPool(DataSourceProperties dataSourceProperties, JdbcUrl jdbcUrl) {
    this.dataSourceProperties = dataSourceProperties;
    this.jdbcUrl = jdbcUrl;
  }

  // TODO: rename connect and disconnect -> enable/disable
  public void connect() {
    log.atInfo().log("Connecting to database '{}'...", jdbcUrl);

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcUrl.get());
    hikariConfig.setConnectionTimeout(
        dataSourceProperties.getConnectionPool().getConnectionTimeout());
    hikariConfig.setMaximumPoolSize(dataSourceProperties.getConnectionPool().getPoolSize());
    hikariConfig.setUsername(dataSourceProperties.getDbmsServer().getCredentials().getUsername());
    hikariConfig.setPassword(dataSourceProperties.getDbmsServer().getCredentials().getPassword());
    hikariDataSource = new HikariDataSource(hikariConfig);

    log.atInfo().log("Connected to the database successfully.");
  }

  public void disconnect() {
    if (hikariDataSource == null) {
      log.atWarn().log("Database disconnection impossible: no existing connection.");
      return;
    }
    if (hikariDataSource.isClosed()) {
      log.atWarn().log("Database disconnection impossible: connection already closed.");
      return;
    }
    hikariDataSource.close();
    log.atInfo().log("Disconnected from the database '{}'.", hikariDataSource.getJdbcUrl());
  }

  public void useConnection(@NonNull Consumer<Connection> consumer) {
    if (hikariDataSource == null) {
      throw SqlStorageException.connectionPoolNotSetup();
    }

    try (Connection connection = hikariDataSource.getConnection()) {
      consumer.accept(connection);
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionLifecycleManagement(e);
    }
  }

  public <T> @NonNull Optional<T> useConnection(
      @NonNull Function<Connection, Optional<T>> function) {
    if (hikariDataSource == null) {
      throw SqlStorageException.connectionPoolNotSetup();
    }

    try (Connection connection = hikariDataSource.getConnection()) {
      return function.apply(connection);
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionLifecycleManagement(e);
    }
  }

  public @NonNull DataSource getDataSource() {
    return hikariDataSource;
  }
}
