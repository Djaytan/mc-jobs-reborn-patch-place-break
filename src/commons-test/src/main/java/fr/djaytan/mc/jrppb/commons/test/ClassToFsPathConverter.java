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
package fr.djaytan.mc.jrppb.commons.test;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/** Converts a class name to a filesystem path with {@link #convertClassToFsPath(Class)}. */
final class ClassToFsPathConverter {

  private static final Pattern DOTS_REGEX_PATTERN = Pattern.compile("\\.");
  private static final Pattern INNER_CLASS_REGEX_PATTERN = Pattern.compile("\\$.*");

  private final FileSystem fileSystem;

  ClassToFsPathConverter(@NotNull FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * Converts a class name to a filesystem path.
   *
   * <p><i>Example: <br>
   * - Class name: fr.djaytan.mc.jrppb.core.config.annotated.HostValidatingPropertiesTest <br>
   * - Equivalent filesystem path:
   * fr/djaytan/mc/jrppb/core/config/annotated/HostValidatingPropertiesTest
   *
   * @param clazz The class with name to be converted to a filesystem path.
   * @return The converted class name to a filesystem path.
   * @throws UnsupportedOperationException If a primary, array, collection or any generic class is
   *     specified.
   */
  @NotNull
  Path convertClassToFsPath(@NotNull Class<?> clazz) {
    checkIfClassIsSupported(clazz);

    String className = clazz.getName();
    String pathStr = getWithInnerClassesFiltered(getWithDotsReplacedBySlashes(className));
    return fileSystem.getPath(pathStr);
  }

  private static @NotNull String getWithDotsReplacedBySlashes(@NotNull String className) {
    return DOTS_REGEX_PATTERN.matcher(className).replaceAll("/");
  }

  private static @NotNull String getWithInnerClassesFiltered(@NotNull String className) {
    return INNER_CLASS_REGEX_PATTERN.matcher(className).replaceAll("");
  }

  private static void checkIfClassIsSupported(@NotNull Class<?> clazz) {
    if (clazz.isArray() || clazz.isPrimitive() || clazz.isAnnotation()) {
      throw new UnsupportedOperationException(
          String.format(
              "The class '%s' isn't supported for resource conversion.", clazz.getTypeName()));
    }
  }
}
