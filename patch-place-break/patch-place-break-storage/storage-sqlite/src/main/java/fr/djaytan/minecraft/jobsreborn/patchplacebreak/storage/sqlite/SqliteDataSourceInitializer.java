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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.ConnectionPool;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlStorageException;

@Singleton
public class SqliteDataSourceInitializer extends SqlDataSourceInitializer {

  private final ConnectionPool connectionPool;
  private final Logger logger;
  private final SqliteUtils sqliteUtils;
  private final TagSqliteDao tagSqliteDao;

  @Inject
  SqliteDataSourceInitializer(ConnectionPool connectionPool, Logger logger,
      SqliteUtils sqliteUtils, TagSqliteDao tagSqliteDao) {
    this.connectionPool = connectionPool;
    this.logger = logger;
    this.sqliteUtils = sqliteUtils;
    this.tagSqliteDao = tagSqliteDao;
  }

  @Override
  protected void createDatabaseIfNotExists() throws SqlStorageException {
    Path sqliteDatabasePath = sqliteUtils.getSqliteDatabasePath();
    String sqliteDatabaseFileName = sqliteDatabasePath.getFileName().toString();

    if (Files.exists(sqliteDatabasePath)) {
      return;
    }

    try {
      Files.createFile(sqliteDatabasePath);
      logger.atInfo().log("The SQLite database '{}' has been created successfully.",
          sqliteDatabaseFileName);
    } catch (IOException e) {
      throw SqlStorageException.databaseCreation(e);
    }
  }

  @Override
  protected void createTableIfNotExists() throws SqlStorageException {
    try (Connection connection = connectionPool.getConnection()) {
      try {
        connection.setAutoCommit(false);

        if (tagSqliteDao.isTableExists(connection)) {
          return;
        }
        tagSqliteDao.createTable(connection);
        connection.commit();
        logger.atInfo().log("The SQLite table '{}' has been created successfully.", SQL_TABLE_NAME);
      } catch (SQLException e) {
        throw SqlStorageException.tableCreation(SQL_TABLE_NAME, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }
}
