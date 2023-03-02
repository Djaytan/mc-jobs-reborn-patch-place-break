package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class SqlStorageException extends RuntimeException {

  private static final String CONNECTION_POOL_NOT_SETUP =
      "The connection pool must be setup before using it.";
  private static final String DATABASE_CONNECTION_LIFECYCLE_MANAGEMENT =
      "Something went wrong when managing database connection lifecycle "
          + "(establishment, releasing, ...).";
  private static final String DATABASE_CREATION = "Unable to create the database '%s'.";
  private static final String TABLE_CREATION = "Unable to create the table '%s'.";

  public static @NonNull SqlStorageException connectionPoolNotSetup() {
    return new SqlStorageException(CONNECTION_POOL_NOT_SETUP);
  }

  public static @NonNull SqlStorageException databaseConnectionLifecycleManagement(
      @NonNull Throwable cause) {
    return new SqlStorageException(DATABASE_CONNECTION_LIFECYCLE_MANAGEMENT, cause);
  }

  public static @NonNull SqlStorageException databaseCreation(
      @NonNull String databaseName, @NonNull Throwable cause) {
    String message = String.format(DATABASE_CREATION, databaseName);
    return new SqlStorageException(message, cause);
  }

  public static @NonNull SqlStorageException tableCreation(
      @NonNull String tableName, @NonNull Throwable cause) {
    String message = String.format(TABLE_CREATION, tableName);
    return new SqlStorageException(message, cause);
  }
}
