package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * This class is highly inspired from slf4j-jdk14 module.
 *
 * <p>The purpose is to permit the creation of a SLF4J instance of the Bukkit logger to ease the
 * decoupling.
 */
public final class BukkitLoggerFactory implements ILoggerFactory {

  private static java.util.logging.Logger bukkitLogger;

  public static void provideBukkitLogger(java.util.logging.Logger bukkitLogger) {
    if (BukkitLoggerFactory.bukkitLogger != null) {
      BukkitLoggerFactory.bukkitLogger.warning("The Bukkit logger has already been provided.");
      return;
    }
    BukkitLoggerFactory.bukkitLogger = bukkitLogger;
  }

  @Override
  public Logger getLogger(String name) {
    return new BukkitLoggerAdapter(bukkitLogger);
  }
}
