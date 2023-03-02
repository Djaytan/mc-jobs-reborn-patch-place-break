package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
@Singleton
public class MetricsFacade {

  private static final int BSTATS_ID = 16899;

  private final JavaPlugin javaPlugin;

  @Inject
  public MetricsFacade(JavaPlugin javaPlugin) {
    this.javaPlugin = javaPlugin;
  }

  public void activateMetricsCollection() {
    new Metrics(javaPlugin, BSTATS_ID);
    log.atInfo().log("bStats metrics collection activated.");
  }
}
