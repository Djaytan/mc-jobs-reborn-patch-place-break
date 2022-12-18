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

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class DaoPersistenceException extends PersistenceException {

  private static final String DELETE = "Failed to delete the tag with the following location: %s";
  private static final String FETCH = "Failed to fetch the tag with the following location: %s";
  private static final String PERSIST = "Failed to persist the following tag: %s";

  public static @NonNull DaoPersistenceException delete(@NonNull TagLocation tagLocation,
      @NonNull Throwable cause) {
    String message = String.format(DELETE, tagLocation);
    return new DaoPersistenceException(message, cause);
  }

  public static @NonNull DaoPersistenceException fetch(@NonNull TagLocation tagLocation,
      @NonNull Throwable cause) {
    String message = String.format(FETCH, tagLocation);
    return new DaoPersistenceException(message, cause);
  }

  public static @NonNull DaoPersistenceException persist(@NonNull Tag tag,
      @NonNull Throwable cause) {
    String message = String.format(PERSIST, tag);
    return new DaoPersistenceException(message, cause);
  }
}
