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

import static fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlDataSourceInitializer.SQL_DATABASE_NAME;

import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import lombok.NonNull;

@Singleton
public class SqliteUtils {

  private static final String SQLITE_DATABASE_FILE_NAME_FORMAT = "%s.db";

  private final Path dataFolder;

  @Inject
  SqliteUtils(@Named("dataFolder") Path dataFolder) {
    this.dataFolder = dataFolder;
  }

  public @NonNull Path getSqliteDatabasePath() {
    return dataFolder.resolve(String.format(SQLITE_DATABASE_FILE_NAME_FORMAT, SQL_DATABASE_NAME));
  }

  public @NonNull String getJdbcUrl() {
    Path sqliteDatabasePath = getSqliteDatabasePath();
    return String.format("jdbc:sqlite:%s", sqliteDatabasePath);
  }
}
