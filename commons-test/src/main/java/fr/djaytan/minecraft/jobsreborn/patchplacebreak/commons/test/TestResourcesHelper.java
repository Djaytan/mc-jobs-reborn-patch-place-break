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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import lombok.NonNull;
import lombok.SneakyThrows;

/**
 * A helper class for resources management.
 *
 * <p>The methods {@link #getResourceFromTestClassAsPath(Class, String)} (Class, String)} and
 * {@link #getResourceFromTestClassAsString(Class, String)} permits to retrieve a resource
 * associated to a given test class.
 */
public final class TestResourcesHelper {

  private static final Pattern DOTS_REGEX_PATTERN = Pattern.compile("\\.");
  private static final Pattern INNER_CLASS_REGEX_PATTERN = Pattern.compile("\\$.*");

  private TestResourcesHelper() {
    // Static class
  }

  /**
   * Retrieves the sought resource path with resource being associated to the specified test class.
   *
   * <p>Resources of a given test class are located at the same folder hierarchy level as for
   * the package one, but in the "resources" folder.
   *
   * <p><i>Example: <br/>
   * - Package: fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.annotated.HostValidatingPropertiesTest<br/>
   * - Equivalent "resources" folder: fr/djaytan/minecraft/jobsreborn/patchplacebreak/core/config/annotated/HostValidatingPropertiesTest
   *
   * @param testClass The test class from which to retrieve a resource.
   * @param resourceName The sought resource name.
   * @return The sought resource path with resource being associated to the specified test class.
   */
  @SneakyThrows
  public static @NonNull Path getResourceFromTestClassAsPath(@NonNull Class<?> testClass,
      @NonNull String resourceName) {
    String testClassResourcesFolder = getTestClassResourcesFolder(testClass);
    Path relativeResourcePath = Paths.get(testClassResourcesFolder, resourceName);
    URL resourceUrl =
        IOUtils.resourceToURL(relativeResourcePath.toString(), testClass.getClassLoader());
    return Paths.get(resourceUrl.toURI());
  }

  /**
   * Retrieves the sought resource as a string encoded with UTF_8 charset with resource being
   * associated to the specified test class.
   *
   * <p>Resources of a given test class are located at the same folder hierarchy level as for
   * the package one, but in the "resources" folder.
   *
   * <p><i>Example: <br/>
   * - Package: fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.annotated.HostValidatingPropertiesTest<br/>
   * - Equivalent "resources" folder: fr/djaytan/minecraft/jobsreborn/patchplacebreak/core/config/annotated/HostValidatingPropertiesTest
   *
   * @param testClass The test class from which to retrieve a resource.
   * @param resourceName The sought resource name.
   * @return The sought resource as string encoded with UTF_8 charset with resource being associated
   *    to the specified test class.
   */
  @SneakyThrows
  public static @NonNull String getResourceFromTestClassAsString(@NonNull Class<?> testClass,
      @NonNull String resourceName) {
    String testClassResourcesFolder = getTestClassResourcesFolder(testClass);
    Path relativeResourcePath = Paths.get(testClassResourcesFolder, resourceName);
    return IOUtils.resourceToString(relativeResourcePath.toString(), StandardCharsets.UTF_8,
        testClass.getClassLoader());
  }

  private static @NonNull String getTestClassResourcesFolder(@NonNull Class<?> testClass) {
    String className = testClass.getName();
    return getWithInnerClassesFiltered(getWithDotsReplacedBySlashes(className));
  }

  private static @NonNull String getWithDotsReplacedBySlashes(@NonNull String className) {
    return DOTS_REGEX_PATTERN.matcher(className).replaceAll("/");
  }

  private static @NonNull String getWithInnerClassesFiltered(@NonNull String className) {
    return INNER_CLASS_REGEX_PATTERN.matcher(className).replaceAll("");
  }
}
