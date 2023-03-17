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
}
