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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.google.common.io.CharStreams;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.PatchPlaceAndBreakRuntimeException;

@Singleton
public class SQLiteDataSourceInitializer implements SqlDataSourceInitializer {

  private final Path dataFolder;
  private final JavaPlugin javaPlugin;
  private final SQLiteDataSource sqliteDataSource;

  @Inject
  public SQLiteDataSourceInitializer(@NotNull @Named("dataFolder") Path dataFolder,
      @NotNull JavaPlugin javaPlugin, @NotNull SQLiteDataSource sqliteDataSource) {
    this.dataFolder = dataFolder;
    this.javaPlugin = javaPlugin;
    this.sqliteDataSource = sqliteDataSource;
  }

  @Override
  public void initialize() {
    Path sqliteDatabasePath = dataFolder.resolve("data.db");

    try {
      if (Files.notExists(dataFolder)) {
        Files.createDirectory(dataFolder);
      }

      if (Files.notExists(sqliteDatabasePath)) {
        Files.createFile(sqliteDatabasePath);
      }
    } catch (IOException e) {
      throw new PatchPlaceAndBreakRuntimeException("Unable to create SQLite database file", e);
    }
  }

  @Override
  public boolean createTablesIfNotExists() {
    try (Connection connection = sqliteDataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sqlQuery = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?";

      try (PreparedStatement query = connection.prepareStatement(sqlQuery)) {
        query.setString(1, SqlDataSource.TABLE_NAME);
        ResultSet rs = query.executeQuery();
        boolean isTableExists = rs.next();

        if (isTableExists) {
          return false;
        }
      }

      InputStream sqlScriptStream = javaPlugin.getResource("database.sql");
      if (sqlScriptStream == null) {
        throw new PatchPlaceAndBreakRuntimeException("Resource 'database.sql' not found.");
      }

      String dbSqlScript;
      try (Reader reader = new InputStreamReader(sqlScriptStream, StandardCharsets.UTF_8)) {
        dbSqlScript = CharStreams.toString(reader);
      }

      try (Statement dbSqlScriptStmt = connection.createStatement()) {
        dbSqlScriptStmt.addBatch(dbSqlScript);
        dbSqlScriptStmt.executeBatch();
        connection.commit();
      }
      return true;

    } catch (SQLException | IOException e) {
      throw new PatchPlaceAndBreakRuntimeException(
          String.format("Failed to create default table: '%s'.", SqlDataSource.TABLE_NAME), e);
    }
  }
}
