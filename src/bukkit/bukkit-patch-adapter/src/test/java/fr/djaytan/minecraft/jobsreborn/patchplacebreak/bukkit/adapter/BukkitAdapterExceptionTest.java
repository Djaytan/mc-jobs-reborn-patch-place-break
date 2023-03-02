package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import lombok.NonNull;

@SuppressWarnings("java:S2187")
class BukkitAdapterExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new BukkitAdapterException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new BukkitAdapterException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new BukkitAdapterException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new BukkitAdapterException(message, cause);
  }
}
