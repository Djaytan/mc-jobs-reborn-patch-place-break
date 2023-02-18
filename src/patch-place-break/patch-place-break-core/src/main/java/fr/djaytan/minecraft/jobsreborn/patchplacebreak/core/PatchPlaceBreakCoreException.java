/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.api.properties.DataSourceType;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class PatchPlaceBreakCoreException extends RuntimeException {

  private static final String INJECTION_REQUIRED_FIRST =
      "The dependencies injection must be achieved first.";
  private static final String UNRECOGNISED_DATA_SOURCE_TYPE = "Unrecognised data source type: %s";
  private static final String UNSUPPORTED_DATA_SOURCE_TYPE = "Unsupported data source type: %s";

  public static @NonNull PatchPlaceBreakCoreException injectionRequiredFirst() {
    return new PatchPlaceBreakCoreException(INJECTION_REQUIRED_FIRST);
  }

  public static @NonNull PatchPlaceBreakCoreException unrecognisedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNRECOGNISED_DATA_SOURCE_TYPE, dataSourceType.name());
    return new PatchPlaceBreakCoreException(message);
  }

  public static @NonNull PatchPlaceBreakCoreException unsupportedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNSUPPORTED_DATA_SOURCE_TYPE, dataSourceType.name());
    UnsupportedOperationException e = new UnsupportedOperationException(message);
    return new PatchPlaceBreakCoreException(e);
  }
}
