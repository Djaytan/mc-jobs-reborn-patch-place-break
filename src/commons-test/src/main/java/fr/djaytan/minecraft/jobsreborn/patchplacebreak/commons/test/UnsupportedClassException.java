package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class UnsupportedClassException extends RuntimeException {

  private static final String UNSUPPORTED_CLASS =
      "The class '%s' isn't supported for resource conversion.";

  public static @NonNull UnsupportedClassException unsupportedClass(@NonNull Class<?> clazz) {
    String message = String.format(UNSUPPORTED_CLASS, clazz.getSimpleName());
    return new UnsupportedClassException(message);
  }
}
