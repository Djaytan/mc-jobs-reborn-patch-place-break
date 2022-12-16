/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.PersistenceException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.SqlDataSource;
import lombok.NonNull;

@Singleton
public class SqliteDataSource implements SqlDataSource {

  private final String createTableSqlScript;
  private final Logger logger;
  private final SqliteDataSourceUtils sqliteDataSourceUtils;

  private HikariDataSource hikariDataSource;

  @Inject
  public SqliteDataSource(@Named("createTableSqlScript") String createTableSqlScript, Logger logger,
      SqliteDataSourceUtils sqliteDataSourceUtils) {
    this.createTableSqlScript = createTableSqlScript;
    this.logger = logger;
    this.sqliteDataSourceUtils = sqliteDataSourceUtils;
  }

  @Override
  public void connect() {
    if (hikariDataSource != null) {
      logger.atWarn().log("SQLite connection has already been established."
          + " Please open an issue to GitHub for investigation.");
      return;
    }

    createDatabaseIfNotExists();
    startConnectionPool();
    createTableIfNotExists();
  }

  private void createDatabaseIfNotExists() {
    Path sqliteDatabasePath = sqliteDataSourceUtils.getSqliteDatabasePath();
    String sqliteDatabaseFileName = sqliteDatabasePath.getFileName().toString();

    if (Files.exists(sqliteDatabasePath)) {
      return;
    }

    try {
      Files.createFile(sqliteDatabasePath);
      logger.atInfo().log("The SQLite database '{}' has been created successfully.",
          sqliteDatabaseFileName);
    } catch (IOException e) {
      throw SqlitePersistenceException.databaseCreation(e);
    }
  }

  private void startConnectionPool() {
    String jdbcUrl = sqliteDataSourceUtils.getJdbcUrl();

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcUrl);
    hikariConfig.setMaximumPoolSize(10);
    hikariConfig.setMinimumIdle(1);
    hikariDataSource = new HikariDataSource(hikariConfig);

    logger.atInfo().log("Connected to the database '{}'.", jdbcUrl);
  }

  private void createTableIfNotExists() {
    try (Connection connection = getConnection()) {
      try {
        connection.setAutoCommit(false);

        if (isTableExists(connection)) {
          return;
        }

        createTable(connection);
        logger.atInfo().log("The SQLite table '{}' has been created successfully.", SQL_TABLE_NAME);
      } catch (SQLException e) {
        throw PersistenceException.tableCreation(SQL_TABLE_NAME, e);
      }
    } catch (SQLException e) {
      throw PersistenceException.databaseConnectionReleasing(e);
    }
  }

  private boolean isTableExists(@NonNull Connection connection) throws SQLException {
    String sqlQuery = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?";

    try (PreparedStatement query = connection.prepareStatement(sqlQuery)) {
      query.setString(1, SQL_TABLE_NAME);
      ResultSet rs = query.executeQuery();
      return rs.next();
    }
  }

  private void createTable(@NonNull Connection connection) throws SQLException {
    try (Statement dbSqlScriptStmt = connection.createStatement()) {
      dbSqlScriptStmt.addBatch(createTableSqlScript);
      dbSqlScriptStmt.executeBatch();
      connection.commit();
    }
  }

  @Override
  public void disconnect() {
    if (hikariDataSource != null) {
      hikariDataSource.close();
      logger.atInfo().log("Disconnected from the database '{}'.", hikariDataSource.getJdbcUrl());
    }
  }

  @Override
  public @NonNull Connection getConnection() {
    if (hikariDataSource == null) {
      throw SqlitePersistenceException.connectionPoolNotSetup();
    }

    try {
      return hikariDataSource.getConnection();
    } catch (SQLException e) {
      throw PersistenceException.databaseConnectionEstablishment(e);
    }
  }
}
