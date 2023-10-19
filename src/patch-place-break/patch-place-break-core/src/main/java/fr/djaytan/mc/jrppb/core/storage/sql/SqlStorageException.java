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
package fr.djaytan.mc.jrppb.core.storage.sql;

import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceType;
import org.jetbrains.annotations.NotNull;

public class SqlStorageException extends RuntimeException {

  private static final String CONNECTION_POOL_NOT_SETUP =
      "The connection pool must be setup before using it";
  private static final String DATABASE_CONNECTION_LIFECYCLE_MANAGEMENT =
      "Something went wrong when managing database connection lifecycle "
          + "(establishment, releasing, ...)";
  private static final String DATABASE_CREATION = "Unable to create the database '%s'";
  private static final String UNSUPPORTED_DATA_SOURCE_TYPE = "Unsupported data source type '%s'";

  private SqlStorageException(@NotNull String message) {
    super(message);
  }

  private SqlStorageException(@NotNull Throwable cause) {
    super(cause);
  }

  private SqlStorageException(@NotNull String message, @NotNull Throwable cause) {
    super(message, cause);
  }

  public static @NotNull SqlStorageException connectionPoolNotSetup() {
    return new SqlStorageException(CONNECTION_POOL_NOT_SETUP);
  }

  public static @NotNull SqlStorageException databaseConnectionLifecycleManagement(
      @NotNull Throwable cause) {
    return new SqlStorageException(DATABASE_CONNECTION_LIFECYCLE_MANAGEMENT, cause);
  }

  public static @NotNull SqlStorageException databaseCreation(
      @NotNull String databaseName, @NotNull Throwable cause) {
    String message = String.format(DATABASE_CREATION, databaseName);
    return new SqlStorageException(message, cause);
  }

  public static @NotNull SqlStorageException unsupportedDataSourceType(
      @NotNull DataSourceType dataSourceType) {
    String message = String.format(UNSUPPORTED_DATA_SOURCE_TYPE, dataSourceType.name());
    UnsupportedOperationException e = new UnsupportedOperationException(message);
    return new SqlStorageException(e);
  }
}
