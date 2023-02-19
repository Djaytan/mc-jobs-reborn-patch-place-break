/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class TagRepositoryException extends RuntimeException {

  private static final String PUT = "Failed to put the following tag: %s";
  private static final String FETCH = "Failed to fetch the tag with the following location: %s";
  private static final String DELETE = "Failed to delete the tag with the following location: %s";

  public static @NonNull TagRepositoryException put(@NonNull Tag tag, @NonNull Throwable cause) {
    String message = String.format(PUT, tag);
    return new TagRepositoryException(message, cause);
  }

  public static @NonNull TagRepositoryException fetch(
      @NonNull TagLocation tagLocation, @NonNull Throwable cause) {
    String message = String.format(FETCH, tagLocation);
    return new TagRepositoryException(message, cause);
  }

  public static @NonNull TagRepositoryException delete(
      @NonNull TagLocation tagLocation, @NonNull Throwable cause) {
    String message = String.format(DELETE, tagLocation);
    return new TagRepositoryException(message, cause);
  }
}
