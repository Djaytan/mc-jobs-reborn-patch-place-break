package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import lombok.NonNull;

@SuppressWarnings("java:S2187")
class SqlStorageExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new SqlStorageException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new SqlStorageException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new SqlStorageException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new SqlStorageException(message, cause);
  }
}
