/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.sqlite;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.sql.TagSqlDataDefiner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public class TagSqliteDataDefiner extends TagSqlDataDefiner {

  @Inject
  public TagSqliteDataDefiner(@NonNull DataSourceProperties dataSourceProperties) {
    super(dataSourceProperties);
  }

  @Override
  public boolean isTableExists(@NonNull Connection connection) throws SQLException {
    String sql = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, dataSourceProperties.getTable());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return resultSet.next();
      }
    }
  }

  public void createTable(@NonNull Connection connection) throws SQLException {
    String sql =
        "CREATE TABLE %s (\n"
            + "  tag_uuid TEXT PRIMARY KEY NOT NULL,\n"
            + "  init_timestamp TEXT NOT NULL,\n"
            + "  is_ephemeral INTEGER NOT NULL,\n"
            + "  world_name TEXT NOT NULL,\n"
            + "  location_x REAL NOT NULL,\n"
            + "  location_y REAL NOT NULL,\n"
            + "  location_z REAL NOT NULL\n"
            + ");";

    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format(sql, dataSourceProperties.getTable()));
    }
  }
}
