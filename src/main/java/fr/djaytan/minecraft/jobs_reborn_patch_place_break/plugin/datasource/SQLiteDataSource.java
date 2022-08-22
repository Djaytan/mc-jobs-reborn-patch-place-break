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

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.PatchPlaceAndBreakRuntimeException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SQLiteDataSource implements SqlDataSource {

  private final Path dataFolder;

  private Connection connection;

  @Inject
  public SQLiteDataSource(@NotNull @Named("dataFolder") Path dataFolder) {
    this.dataFolder = dataFolder;
  }

  @Override
  public void connect() {
    if (connection != null) {
      throw new PatchPlaceAndBreakRuntimeException("SQLite connection already been done.");
    }

    Path sqliteDatabasePath = dataFolder.resolve("data.db");
    String url = String.format("jdbc:sqlite:%s", sqliteDatabasePath);

    try {
      connection = DriverManager.getConnection(url);
    } catch (SQLException e) {
      throw new PatchPlaceAndBreakRuntimeException("Failed to connect SQLite database.", e);
    }
  }

  @Override
  public @NotNull Connection getConnection() {
    if (connection == null) {
      throw new PatchPlaceAndBreakRuntimeException(
          "The connection to SQLite database must be done before using it.");
    }
    return connection;
  }
}
