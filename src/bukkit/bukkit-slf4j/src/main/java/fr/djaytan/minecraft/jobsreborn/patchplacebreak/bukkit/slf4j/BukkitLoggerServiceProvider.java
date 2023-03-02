package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * This class is highly inspired from slf4j-jdk14 module.
 *
 * <p>The purpose is to permit the creation of a SLF4J instance of the Bukkit logger to ease the
 * decoupling.
 */
public class BukkitLoggerServiceProvider implements SLF4JServiceProvider {

  /** Declare the version of the SLF4J API this implementation is compiled against. */
  private static final String REQUESTED_API_VERSION = "2.0.99";

  private ILoggerFactory loggerFactory;
  private IMarkerFactory markerFactory;
  private MDCAdapter mdcAdapter;

  @Override
  public ILoggerFactory getLoggerFactory() {
    return loggerFactory;
  }

  @Override
  public IMarkerFactory getMarkerFactory() {
    return markerFactory;
  }

  public MDCAdapter getMDCAdapter() {
    return mdcAdapter;
  }

  @Override
  public String getRequestedApiVersion() {
    return REQUESTED_API_VERSION;
  }

  @Override
  public void initialize() {
    loggerFactory = new BukkitLoggerFactory();
    markerFactory = new BasicMarkerFactory();
    mdcAdapter = new BasicMDCAdapter();
  }
}
