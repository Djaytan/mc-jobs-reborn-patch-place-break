package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * A helper class for resources management with tests.
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
   * - Class' path:
   * fr/djaytan/minecraft/jobsreborn/patchplacebreak/core/config/annotated/HostValidatingPropertiesTest.class
   * <br>
   * - Class' resources path:
   * fr/djaytan/minecraft/jobsreborn/patchplacebreak/core/config/annotated/HostValidatingPropertiesTest/*
   *
   * @param clazz The class from which to retrieve a resource.
   * @param resourceName The sought resource name.
   * @return The class' resource as an absolute path.
   */
  @SneakyThrows
  public static @NonNull Path getClassResourceAsAbsolutePath(
      @NonNull Class<?> clazz, @NonNull String resourceName) {
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
   * - Class' path:
   * fr/djaytan/minecraft/jobsreborn/patchplacebreak/core/config/annotated/HostValidatingPropertiesTest.class
   * <br>
   * - Class' resources path:
   * fr/djaytan/minecraft/jobsreborn/patchplacebreak/core/config/annotated/HostValidatingPropertiesTest/*
   *
   * @param clazz The class from which to retrieve a resource.
   * @param resourceName The sought resource name.
   * @param chopContent Whether the content must be chopped or not with {@link
   *     StringUtils#chop(String)}
   * @return The class' resource as a string encoded with UTF_8 charset.
   */
  @SneakyThrows
  public static @NonNull String getClassResourceAsString(
      @NonNull Class<?> clazz, @NonNull String resourceName, boolean chopContent) {
    Path resourceRelativePath = getClassResourceAsRelativePath(clazz, resourceName);
    String resourceContent =
        IOUtils.resourceToString(
            resourceRelativePath.toString(), StandardCharsets.UTF_8, clazz.getClassLoader());
    return chopContent ? StringUtils.chop(resourceContent) : resourceContent;
  }

  private static @NonNull Path getClassResourceAsRelativePath(
      @NonNull Class<?> clazz, @NonNull String resourceName) {
    ClassToFsPathConverter classToFsPathConverter =
        new ClassToFsPathConverter(FileSystems.getDefault());
    Path classResourcesFolder = classToFsPathConverter.convertClassToFsPath(clazz);
    return classResourcesFolder.resolve(resourceName);
  }

  private static @NonNull Path resourceToAbsolutePath(
      @NonNull Path relativeResourcePath, @NonNull ClassLoader classLoader)
      throws IOException, URISyntaxException {
    // /!\ Not retrieving absolute path from class loader can lead to wrong path depending on cases
    URL url = IOUtils.resourceToURL(relativeResourcePath.toString(), classLoader);
    return Paths.get(url.toURI());
  }
}
