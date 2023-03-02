package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockBreakListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockGrowListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockPistonListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockPlaceListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockSpreadListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsExpGainListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsPrePaymentListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/** This class represents a register of listeners. */
@Slf4j
@Singleton
public class ListenerRegister {

  private final JavaPlugin javaPlugin;
  private final PluginManager pluginManager;

  private final BlockBreakListener blockBreakListener;
  private final BlockGrowListener blockGrowListener;
  private final BlockPistonListener blockPistonListener;
  private final BlockPlaceListener blockPlaceListener;
  private final BlockSpreadListener blockSpreadListener;
  private final JobsExpGainListener jobsExpGainListener;
  private final JobsPrePaymentListener jobsPrePaymentListener;

  @Inject
  public ListenerRegister(
      JavaPlugin javaPlugin,
      PluginManager pluginManager,
      BlockBreakListener blockBreakListener,
      BlockGrowListener blockGrowListener,
      BlockPistonListener blockPistonListener,
      BlockPlaceListener blockPlaceListener,
      BlockSpreadListener blockSpreadListener,
      JobsExpGainListener jobsExpGainListener,
      JobsPrePaymentListener jobsPrePaymentListener) {
    this.javaPlugin = javaPlugin;
    this.pluginManager = pluginManager;
    this.blockBreakListener = blockBreakListener;
    this.blockGrowListener = blockGrowListener;
    this.blockPistonListener = blockPistonListener;
    this.blockPlaceListener = blockPlaceListener;
    this.blockSpreadListener = blockSpreadListener;
    this.jobsExpGainListener = jobsExpGainListener;
    this.jobsPrePaymentListener = jobsPrePaymentListener;
  }

  /**
   * The purposes of this method is simple: registering listeners through the {@link PluginManager}.
   */
  public void registerListeners() {
    pluginManager.registerEvents(blockBreakListener, javaPlugin);
    pluginManager.registerEvents(blockGrowListener, javaPlugin);
    pluginManager.registerEvents(blockPistonListener, javaPlugin);
    pluginManager.registerEvents(blockPlaceListener, javaPlugin);
    pluginManager.registerEvents(blockSpreadListener, javaPlugin);
    pluginManager.registerEvents(jobsExpGainListener, javaPlugin);
    pluginManager.registerEvents(jobsPrePaymentListener, javaPlugin);
    log.atInfo().log("Event listeners registered.");
  }

  public void reloadListeners() {
    log.atInfo().log("Reloading event listeners...");
    unregisterListeners();
    registerListeners();
    log.atInfo().log("Event listeners reloaded successfully.");
  }

  private void unregisterListeners() {
    HandlerList.unregisterAll(javaPlugin);
    log.atInfo().log("Event listeners unregistered.");
  }
}
