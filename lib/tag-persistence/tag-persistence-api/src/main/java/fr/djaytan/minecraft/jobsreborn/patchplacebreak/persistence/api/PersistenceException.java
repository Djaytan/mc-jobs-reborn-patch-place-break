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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api;

import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class PersistenceException extends PatchPlaceBreakException {

  private static final String TABLE_CREATION = "Unable to create the table '%s'.";
  private static final String DATABASE_CONNECTION_ESTABLISHMENT =
      "Failed to establish connection to the database.";
  private static final String DATABASE_CONNECTION_RELEASING =
      "Something prevent the database connection releasing.";

  public static @NotNull PersistenceException databaseConnectionEstablishment(
      @NonNull Throwable cause) {
    return new PersistenceException(DATABASE_CONNECTION_ESTABLISHMENT, cause);
  }

  public static @NotNull PersistenceException databaseConnectionReleasing(
      @NonNull Throwable cause) {
    return new PersistenceException(DATABASE_CONNECTION_RELEASING, cause);
  }

  public static @NotNull PersistenceException tableCreation(@NonNull String tableName,
      @NonNull Throwable cause) {
    String message = String.format(TABLE_CREATION, tableName);
    return new PersistenceException(message, cause);
  }
}
