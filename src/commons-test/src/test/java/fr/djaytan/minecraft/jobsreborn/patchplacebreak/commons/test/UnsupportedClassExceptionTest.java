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
