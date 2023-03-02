package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreak;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.ListenerRegister;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject.GuiceBukkitInjector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.slf4j.BukkitLoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

/** This class represents a JobsReborn patch place-break plugin. */
@SuppressWarnings("unused") // Instantiated by Bukkit's implementation (i.e. CraftBukkit)
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  @Inject private ListenerRegister listenerRegister;
  @Inject private MetricsFacade metricsFacade;
  @Inject private PatchPlaceBreak patchPlaceBreak;

  @Override
  @SneakyThrows
  public void onEnable() {
    createFolderIfNotExist(getDataFolder().toPath());
    enableSlf4j(getLogger());
    GuiceBukkitInjector.inject(getClassLoader(), this);
    listenerRegister.registerListeners();
    metricsFacade.activateMetricsCollection();
    getLogger().info("JobsReborn-PatchPlaceBreak successfully enabled.");
  }

  @Override
  public void onDisable() {
    patchPlaceBreak.disable();
    getLogger().info("JobsReborn-PatchPlaceBreak successfully disabled.");
  }

  private static void createFolderIfNotExist(@NonNull Path dataFolder) throws IOException {
    Files.createDirectories(dataFolder);
  }

  private static void enableSlf4j(@NonNull Logger bukkitLogger) {
    BukkitLoggerFactory.provideBukkitLogger(bukkitLogger);
  }
}
