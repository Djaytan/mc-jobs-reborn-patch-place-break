package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class MetricsFacade {

  private static final int BSTATS_ID = 16899;

  private final JavaPlugin javaPlugin;
  private final Logger logger;

  @Inject
  public MetricsFacade(@NotNull JavaPlugin javaPlugin,
      @NotNull @Named("PatchPlaceBreakLogger") Logger logger) {
    this.javaPlugin = javaPlugin;
    this.logger = logger;
  }

  public void activateMetricsCollection() {
    new Metrics(javaPlugin, BSTATS_ID);
    logger.info("bStats metrics collection activated.");
  }
}
