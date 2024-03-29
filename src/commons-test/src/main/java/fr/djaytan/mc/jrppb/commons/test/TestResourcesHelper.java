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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.UncheckedException;
import org.jetbrains.annotations.NotNull;

/**
 * A helper class for resource management with tests.
 *
 * <p>The methods {@link #getClassResourceAsAbsolutePath(Class, String)} and {@link
 * #getClassResourceAsString(Class, String, boolean)} permits to retrieve a resource associated to a
 * given class.
 */
public final class TestResourcesHelper {

  private TestResourcesHelper() {
    // Static class
  }

  /**
   * Retrieves class' resource as an absolute path.
   *
   * <p>Resources of a given class are expected to be located in a folder located at the same
   * hierarchy level as for the class.
   *
   * <p><i>Example: <br>
   * - Class' path: fr/djaytan/mc/jrppb/core/config/annotated/HostValidatingPropertiesTest.class
   * <br>
   * - Class' resources path:
   * fr/djaytan/mc/jrppb/core/config/annotated/HostValidatingPropertiesTest/*
   *
   * @param clazz The class from which to retrieve a resource.
   * @param resourceName The sought resource name.
   * @return The class' resource as an absolute path.
   */
  public static @NotNull Path getClassResourceAsAbsolutePath(
      @NotNull Class<?> clazz, @NotNull String resourceName) {
    Path relativeResourcePath = getClassResourceAsRelativePath(clazz, resourceName);
    return resourceToAbsolutePath(relativeResourcePath, clazz.getClassLoader());
  }

  /**
   * Retrieves class' resource as a string encoded with UTF_8 charset.
   *
   * <p>Resources of a given class are expected to be located in a folder located at the same
   * hierarchy level as for the class.
   *
   * <p><i>Example: <br>
   * - Class' path: fr/djaytan/mc/jrppb/core/config/annotated/HostValidatingPropertiesTest.class
   * <br>
   * - Class' resources path:
   * fr/djaytan/mc/jrppb/core/config/annotated/HostValidatingPropertiesTest/*
   *
   * @param clazz The class from which to retrieve a resource.
   * @param resourceName The sought resource name.
   * @param chopContent Whether the content must be chopped or not with {@link
   *     StringUtils#chop(String)}
   * @return The class' resource as a string encoded with UTF_8 charset.
   */
  public static @NotNull String getClassResourceAsString(
      @NotNull Class<?> clazz, @NotNull String resourceName, boolean chopContent) {
    try {
      Path resourceRelativePath = getClassResourceAsRelativePath(clazz, resourceName);
      String resourceContent =
          IOUtils.resourceToString(
              resourceRelativePath.toString(), StandardCharsets.UTF_8, clazz.getClassLoader());
      return chopContent ? StringUtils.chop(resourceContent) : resourceContent;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static @NotNull Path getClassResourceAsRelativePath(
      @NotNull Class<?> clazz, @NotNull String resourceName) {
    ClassToFsPathConverter classToFsPathConverter =
        new ClassToFsPathConverter(FileSystems.getDefault());
    Path classResourcesFolder = classToFsPathConverter.convertClassToFsPath(clazz);
    return classResourcesFolder.resolve(resourceName);
  }

  private static @NotNull Path resourceToAbsolutePath(
      @NotNull Path relativeResourcePath, @NotNull ClassLoader classLoader) {
    try {
      // /!\ Not retrieving an absolute path from class loader can lead to a wrong path depending on
      // cases
      // (i.e. when working directory of a library isn't the same as the one used by this class)
      URL url = IOUtils.resourceToURL(relativeResourcePath.toString(), classLoader);
      return Paths.get(url.toURI());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (URISyntaxException e) {
      throw new UncheckedException(e);
    }
  }
}
