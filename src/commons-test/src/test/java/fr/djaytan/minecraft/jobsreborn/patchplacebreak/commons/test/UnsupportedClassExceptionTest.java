/*-
 * #%L
 * JobsReborn-PatchPlaceBreak
 * %%
 * Copyright (C) 2022 - 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 * %%
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
 * #L%
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import lombok.NonNull;

@SuppressWarnings("java:S2187")
class UnsupportedClassExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new UnsupportedClassException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new UnsupportedClassException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new UnsupportedClassException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new UnsupportedClassException(message, cause);
  }
}
