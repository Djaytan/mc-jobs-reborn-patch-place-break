package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.regex.Pattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/** Converts a class name to a filesystem path with {@link #convertClassToFsPath(Class)}. */
@RequiredArgsConstructor
final class ClassToFsPathConverter {

  private static final Pattern DOTS_REGEX_PATTERN = Pattern.compile("\\.");
  private static final Pattern INNER_CLASS_REGEX_PATTERN = Pattern.compile("\\$.*");

  private final FileSystem fileSystem;

  /**
   * Converts a class name to a filesystem path.
   *
   * <p><i>Example: <br>
   * - Class name:
   * fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.annotated.HostValidatingPropertiesTest
   * <br>
   * - Equivalent filesystem path:
   * fr/djaytan/minecraft/jobsreborn/patchplacebreak/core/config/annotated/HostValidatingPropertiesTest
   *
   * @param clazz The class with name to be converted to filesystem path.
   * @return The converted class name to a filesystem path.
   * @throws UnsupportedClassException If a primary, array, collection or any generic class is
   *     specified.
   */
  @NonNull
  Path convertClassToFsPath(@NonNull Class<?> clazz) {
    checkIfClassIsSupported(clazz);

    String className = clazz.getName();
    String pathStr = getWithInnerClassesFiltered(getWithDotsReplacedBySlashes(className));
    return fileSystem.getPath(pathStr);
  }

  private static @NonNull String getWithDotsReplacedBySlashes(@NonNull String className) {
    return DOTS_REGEX_PATTERN.matcher(className).replaceAll("/");
  }

  private static @NonNull String getWithInnerClassesFiltered(@NonNull String className) {
    return INNER_CLASS_REGEX_PATTERN.matcher(className).replaceAll("");
  }

  private static void checkIfClassIsSupported(@NonNull Class<?> clazz) {
    if (clazz.isArray() || clazz.isPrimitive() || clazz.isAnnotation()) {
      throw UnsupportedClassException.unsupportedClass(clazz);
    }
  }
}
