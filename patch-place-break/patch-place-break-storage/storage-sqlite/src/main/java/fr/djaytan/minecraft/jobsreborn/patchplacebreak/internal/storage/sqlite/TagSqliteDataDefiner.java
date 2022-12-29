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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.TagSqlDataDefiner;
import lombok.NonNull;

@Singleton
public class TagSqliteDataDefiner extends TagSqlDataDefiner {

  private static final String UNSUPPORTED_SQL_DATABASE_CREATION =
      "Database creation through SQL isn't supported with SQLite.";

  @Inject
  TagSqliteDataDefiner(DataSourceProperties dataSourceProperties) {
    super(dataSourceProperties);
  }

  @Override
  public boolean isDatabaseExists(@NonNull Connection connection) {
    throw new UnsupportedOperationException(UNSUPPORTED_SQL_DATABASE_CREATION);
  }

  @Override
  public void createDatabase(@NonNull Connection connection) {
    throw new UnsupportedOperationException(UNSUPPORTED_SQL_DATABASE_CREATION);
  }

  @Override
  public boolean isTableExists(@NonNull Connection connection) throws SQLException {
    String sql = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, dataSourceProperties.getTableName());
      ResultSet rs = preparedStatement.executeQuery();
      return rs.next();
    }
  }
}
