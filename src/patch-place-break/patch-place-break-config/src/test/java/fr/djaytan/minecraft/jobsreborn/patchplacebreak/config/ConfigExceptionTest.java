package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import lombok.NonNull;

@SuppressWarnings("java:S2187")
class ConfigExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new ConfigException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new ConfigException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new ConfigException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new ConfigException(message, cause);
  }
}
