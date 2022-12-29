package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class SqlHelper {

  private final ConnectionPool connectionPool;
  private final DataSourceProperties dataSourceProperties;
  private final TagSqlDataDefiner tagSqlDataDefiner;

  @Inject
  SqlHelper(ConnectionPool connectionPool, DataSourceProperties dataSourceProperties,
      TagSqlDataDefiner tagSqlDataDefiner) {
    this.connectionPool = connectionPool;
    this.dataSourceProperties = dataSourceProperties;
    this.tagSqlDataDefiner = tagSqlDataDefiner;
  }

  public void createDatabaseIfNotExists() throws SqlStorageException {
    String databaseName = dataSourceProperties.getDatabaseName();

    try (Connection connection = connectionPool.getConnection()) {
      try {
        connection.setAutoCommit(false);

        if (tagSqlDataDefiner.isDatabaseExists(connection)) {
          return;
        }
        tagSqlDataDefiner.createDatabase(connection);
        connection.commit();
        log.atInfo().log("The SQL database '{}' has been created successfully.", databaseName);
      } catch (SQLException e) {
        throw SqlStorageException.databaseCreation(databaseName, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }

  public void switchToDatabase() throws SqlStorageException {
    String databaseName = dataSourceProperties.getDatabaseName();

    try (Connection connection = connectionPool.getConnection()) {
      try {
        tagSqlDataDefiner.useDatabase(connection);
        log.atInfo().log("Switching to the database '{}'.", databaseName);
      } catch (SQLException e) {
        throw SqlStorageException.databaseSwitch(databaseName, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }

  public void createTableIfNotExists() throws SqlStorageException {
    String tableName = dataSourceProperties.getTableName();

    try (Connection connection = connectionPool.getConnection()) {
      try {
        connection.setAutoCommit(false);

        if (tagSqlDataDefiner.isTableExists(connection)) {
          return;
        }
        tagSqlDataDefiner.createTable(connection);
        connection.commit();
        log.atInfo().log("The SQL table '{}' has been created successfully.", tableName);
      } catch (SQLException e) {
        throw SqlStorageException.tableCreation(tableName, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }
}
