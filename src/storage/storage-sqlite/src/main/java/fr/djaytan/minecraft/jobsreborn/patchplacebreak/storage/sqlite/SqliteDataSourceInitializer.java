/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlStorageException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

@Slf4j
@Singleton
public final class SqliteDataSourceInitializer implements DataSourceInitializer {

  private final DataSourceProperties dataSourceProperties;
  private final Path sqliteDatabaseFile;

  @Inject
  public SqliteDataSourceInitializer(
      DataSourceProperties dataSourceProperties,
      @Named("sqliteDatabaseFile") Path sqliteDatabaseFile) {
    this.dataSourceProperties = dataSourceProperties;
    this.sqliteDatabaseFile = sqliteDatabaseFile;
  }

  @Override
  public void initialize() {
    Validate.validState(
        dataSourceProperties.getType() == DataSourceType.SQLITE,
        "The data source type is expected to be 'SQLITE'.");

    createDatabaseIfNotExists();
  }

  private void createDatabaseIfNotExists() {
    String sqliteDatabaseFileName = sqliteDatabaseFile.getFileName().toString();

    if (Files.exists(sqliteDatabaseFile)) {
      return;
    }

    log.atInfo().log("No SQLite database '{}' found. Creating it...", sqliteDatabaseFileName);

    try {
      Files.createFile(sqliteDatabaseFile);
      log.atInfo().log(
          "The SQLite database '{}' has been created successfully.", sqliteDatabaseFileName);
    } catch (IOException e) {
      throw SqlStorageException.databaseCreation(sqliteDatabaseFileName, e);
    }
  }
}
