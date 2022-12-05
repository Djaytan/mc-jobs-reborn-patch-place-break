package fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite;

import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

@Singleton
public class SqliteDataSourceUtils {

  private static final String SQLITE_DATABASE_FILE_NAME = "data.db";

  private final Path dataFolder;

  @Inject
  public SqliteDataSourceUtils(@NotNull @Named("dataFolder") Path dataFolder) {
    this.dataFolder = dataFolder;
  }

  public @NotNull Path getSqliteDatabasePath() {
    return dataFolder.resolve(SQLITE_DATABASE_FILE_NAME);
  }

  public @NotNull String getJdbcUrl() {
    Path sqliteDatabasePath = getSqliteDatabasePath();
    return String.format("jdbc:sqlite:%s", sqliteDatabasePath);
  }
}
