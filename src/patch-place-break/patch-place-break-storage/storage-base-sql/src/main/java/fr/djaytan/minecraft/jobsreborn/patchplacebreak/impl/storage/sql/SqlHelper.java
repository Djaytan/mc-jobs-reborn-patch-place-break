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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.api.properties.DataSourceProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class SqlHelper {

  private final ConnectionPool connectionPool;
  private final DataSourceProperties dataSourceProperties;
  private final TagSqlDataDefiner tagSqlDataDefiner;

  @Inject
  public SqlHelper(@NonNull ConnectionPool connectionPool,
      @NonNull DataSourceProperties dataSourceProperties,
      @NonNull TagSqlDataDefiner tagSqlDataDefiner) {
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
