package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import lombok.NonNull;

@SuppressWarnings("java:S2187")
class TagRepositoryExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new TagRepositoryException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new TagRepositoryException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new TagRepositoryException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new TagRepositoryException(message, cause);
  }
}
