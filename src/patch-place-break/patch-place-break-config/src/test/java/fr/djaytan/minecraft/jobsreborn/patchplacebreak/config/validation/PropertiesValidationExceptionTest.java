package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import lombok.NonNull;

@SuppressWarnings("java:S2187")
class PropertiesValidationExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new PropertiesValidationException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new PropertiesValidationException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new PropertiesValidationException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new PropertiesValidationException(message, cause);
  }
}
