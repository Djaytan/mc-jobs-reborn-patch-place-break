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

  public void createTableIfNotExists() throws SqlStorageException {
    String table = dataSourceProperties.getTable();

    try (Connection connection = connectionPool.getConnection()) {
      try {
        connection.setAutoCommit(false);

        if (tagSqlDataDefiner.isTableExists(connection)) {
          return;
        }
        tagSqlDataDefiner.createTable(connection);
        connection.commit();
        log.atInfo().log("The SQL table '{}' has been created successfully.", table);
      } catch (SQLException e) {
        throw SqlStorageException.tableCreation(table, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }
}
