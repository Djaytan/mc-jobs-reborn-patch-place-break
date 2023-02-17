/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.TagSqlDataDefiner;
import lombok.NonNull;

@Singleton
public class TagMysqlDataDefiner extends TagSqlDataDefiner {

  @Inject
  public TagMysqlDataDefiner(DataSourceProperties dataSourceProperties) {
    super(dataSourceProperties);
  }

  @Override
  public boolean isTableExists(@NonNull Connection connection) throws SQLException {
    String sql = "SELECT table_name " + "FROM information_schema.tables "
        + "WHERE table_schema = ? " + "AND table_type = 'BASE TABLE' " + "AND table_name = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, dataSourceProperties.getDbmsServer().getDatabase());
      preparedStatement.setString(2, dataSourceProperties.getTable());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return resultSet.next();
      }
    }
  }

  public void createTable(@NonNull Connection connection) throws SQLException {
    String sql = "CREATE TABLE %s (\n" + "  tag_uuid CHAR(36) PRIMARY KEY NOT NULL,\n"
        + "  init_timestamp VARCHAR(64) NOT NULL,\n" + "  is_ephemeral INTEGER NOT NULL,\n"
        + "  world_name VARCHAR(128) NOT NULL,\n" + "  location_x DOUBLE NOT NULL,\n"
        + "  location_y DOUBLE NOT NULL,\n" + "  location_z DOUBLE NOT NULL\n" + ");";

    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format(sql, dataSourceProperties.getTable()));
    }
  }
}
