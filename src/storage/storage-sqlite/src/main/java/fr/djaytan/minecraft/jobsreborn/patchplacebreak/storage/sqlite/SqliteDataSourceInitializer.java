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
