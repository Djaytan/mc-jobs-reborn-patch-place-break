/*
 * JobsReborn extension to patch place-break (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakException;

@Singleton
public class SqliteDataSource implements SqlDataSource {

  private final String createTableSqlScript;
  private final Logger logger;
  private final SqliteDataSourceUtils sqliteDataSourceUtils;

  private HikariDataSource hikariDataSource;

  @Inject
  public SqliteDataSource(@NotNull @Named("createTableSqlScript") String createTableSqlScript,
      @NotNull @Named("PatchPlaceBreakLogger") Logger logger,
      @NotNull SqliteDataSourceUtils sqliteDataSourceUtils) {
    this.createTableSqlScript = createTableSqlScript;
    this.logger = logger;
    this.sqliteDataSourceUtils = sqliteDataSourceUtils;
  }

  @Override
  public void connect() {
    if (hikariDataSource != null) {
      throw new PatchPlaceBreakException("SQLite connection already been done.");
    }

    createDatabaseIfNotExists();
    startConnectionPool();
    createTableIfNotExists();
  }

  private void createDatabaseIfNotExists() {
    Path sqliteDatabasePath = sqliteDataSourceUtils.getSqliteDatabasePath();
    String sqliteDatabaseFileName = sqliteDatabasePath.getFileName().toString();

    if (Files.exists(sqliteDatabasePath)) {
      logger.info(
          String.format("The SQLite database '%s' already exists: skipping database creation.",
              sqliteDatabaseFileName));
    }

    try {
      Files.createFile(sqliteDatabasePath);
      logger.info(String.format("The SQLite database '%s' has been created successfully.",
          sqliteDatabaseFileName));
    } catch (IOException e) {
      throw new PatchPlaceBreakException("Unable to create the SQLite database.", e);
    }
  }

  private void startConnectionPool() {
    String jdbcUrl = sqliteDataSourceUtils.getJdbcUrl();

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcUrl);
    hikariConfig.setMaximumPoolSize(10);
    hikariConfig.setMinimumIdle(1);
    hikariDataSource = new HikariDataSource(hikariConfig);
  }

  private void createTableIfNotExists() {
    try (Connection connection = getConnection()) {
      connection.setAutoCommit(false);

      if (isTableExists(connection)) {
        logger.info(String.format(
            "The SQLite table '%s' already exists: skipping the table creation.", SQL_TABLE_NAME));
        return;
      }

      createTable(connection);
      logger.info(
          String.format("The SQLite table '%s' has been created successfully.", SQL_TABLE_NAME));
    } catch (SQLException e) {
      throw new PatchPlaceBreakException(
          String.format("Failed to create the table '%s'.", SqlDataSource.SQL_TABLE_NAME), e);
    }
  }

  private boolean isTableExists(@NotNull Connection connection) throws SQLException {
    String sqlQuery = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?";

    try (PreparedStatement query = connection.prepareStatement(sqlQuery)) {
      query.setString(1, SqlDataSource.SQL_TABLE_NAME);
      ResultSet rs = query.executeQuery();
      return rs.next();
    }
  }

  private void createTable(@NotNull Connection connection) throws SQLException {
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
    }
  }

  @Override
  public @NotNull Connection getConnection() {
    if (hikariDataSource == null) {
      // TODO: create PersistenceException in persistence-api instead
      throw new PatchPlaceBreakException("The connection pool must be setup before using it.");
    }
    try {
      return hikariDataSource.getConnection();
    } catch (SQLException e) {
      throw new PatchPlaceBreakException("Failed to establish connection to the SQLite database.",
          e);
    }
  }
}
