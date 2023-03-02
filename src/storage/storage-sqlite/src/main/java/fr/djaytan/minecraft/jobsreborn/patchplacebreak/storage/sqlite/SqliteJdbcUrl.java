package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.NonNull;

/** Represents the SQLite JDBC URL. */
@Singleton
public final class SqliteJdbcUrl implements JdbcUrl {

  private static final String SQLITE_JDBC_URL_FORMAT = "jdbc:sqlite:%s";

  private final Path sqliteDatabaseFile;

  @Inject
  public SqliteJdbcUrl(@Named("sqliteDatabaseFile") Path sqliteDatabaseFile) {
    this.sqliteDatabaseFile = sqliteDatabaseFile;
  }

  /** {@inheritDoc} */
  @Override
  public @NonNull String get() {
    return String.format(SQLITE_JDBC_URL_FORMAT, sqliteDatabaseFile);
  }
}
