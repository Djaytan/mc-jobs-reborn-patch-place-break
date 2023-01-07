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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.SqlStorageException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class SqliteHelper {

  private static final String SQLITE_DATABASE_FILE_NAME = "sqlite-data.db";

  private final Path dataFolder;

  @Inject
  SqliteHelper(@Named("dataFolder") Path dataFolder) {
    this.dataFolder = dataFolder;
  }

  public @NonNull Path getSqliteDatabasePath() {
    return dataFolder.resolve(SQLITE_DATABASE_FILE_NAME);
  }

  public void createDatabaseIfNotExists() throws SqlStorageException {
    Path sqliteDatabasePath = getSqliteDatabasePath();
    String sqliteDatabaseFileName = sqliteDatabasePath.getFileName().toString();

    if (Files.exists(sqliteDatabasePath)) {
      return;
    }

    try {
      Files.createFile(sqliteDatabasePath);
      log.atInfo().log("The SQLite database '{}' has been created successfully.",
          sqliteDatabaseFileName);
    } catch (IOException e) {
      throw SqlStorageException.databaseCreation(sqliteDatabaseFileName, e);
    }
  }
}
