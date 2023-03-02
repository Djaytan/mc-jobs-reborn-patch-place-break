package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import lombok.NonNull;

@SuppressWarnings("java:S2187")
class ConfigSerializationExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new ConfigSerializationException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new ConfigSerializationException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new ConfigSerializationException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new ConfigSerializationException(message, cause);
  }
}
