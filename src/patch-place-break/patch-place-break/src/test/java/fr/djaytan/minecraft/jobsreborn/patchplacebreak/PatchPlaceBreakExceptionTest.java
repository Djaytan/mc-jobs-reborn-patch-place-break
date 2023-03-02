package fr.djaytan.minecraft.jobsreborn.patchplacebreak;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import lombok.NonNull;

@SuppressWarnings("java:S2187")
class PatchPlaceBreakExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new PatchPlaceBreakException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new PatchPlaceBreakException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new PatchPlaceBreakException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new PatchPlaceBreakException(message, cause);
  }
}
