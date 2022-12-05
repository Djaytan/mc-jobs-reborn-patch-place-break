package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit;

import org.jetbrains.annotations.NotNull;

public class BukkitException extends RuntimeException {

  protected BukkitException(String message) {
    super(message);
  }

  public static @NotNull BukkitException resourceNotFound(@NotNull String resourceName) {
    String message = String.format("Plugin's resource '%s' not found.", resourceName);
    return new BukkitException(message);
  }

  public static @NotNull BukkitException malformedResource(@NotNull String resourceName) {
    String message = String.format("Cannot read the resource '%s'.", resourceName);
    return new BukkitException(message);
  }
}
