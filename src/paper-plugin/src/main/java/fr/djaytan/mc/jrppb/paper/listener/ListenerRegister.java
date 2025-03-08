/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.mc.jrppb.paper.listener;

import fr.djaytan.mc.jrppb.paper.listener.block.BlockBreakListener;
import fr.djaytan.mc.jrppb.paper.listener.block.BlockGrowListener;
import fr.djaytan.mc.jrppb.paper.listener.block.BlockPistonListener;
import fr.djaytan.mc.jrppb.paper.listener.block.BlockPlaceListener;
import fr.djaytan.mc.jrppb.paper.listener.block.BlockSpreadListener;
import fr.djaytan.mc.jrppb.paper.listener.jobs.JobsExpGainListener;
import fr.djaytan.mc.jrppb.paper.listener.jobs.JobsPrePaymentListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class represents a register of listeners. */
@Singleton
public class ListenerRegister {

  private static final Logger log = LoggerFactory.getLogger(ListenerRegister.class);

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
      @NotNull JavaPlugin javaPlugin,
      @NotNull PluginManager pluginManager,
      @NotNull BlockBreakListener blockBreakListener,
      @NotNull BlockGrowListener blockGrowListener,
      @NotNull BlockPistonListener blockPistonListener,
      @NotNull BlockPlaceListener blockPlaceListener,
      @NotNull BlockSpreadListener blockSpreadListener,
      @NotNull JobsExpGainListener jobsExpGainListener,
      @NotNull JobsPrePaymentListener jobsPrePaymentListener) {
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
   * The purpose of this method is simple: registering listeners through the {@link PluginManager}.
   */
  public void registerListeners() {
    pluginManager.registerEvents(blockBreakListener, javaPlugin);
    pluginManager.registerEvents(blockGrowListener, javaPlugin);
    pluginManager.registerEvents(blockPistonListener, javaPlugin);
    pluginManager.registerEvents(blockPlaceListener, javaPlugin);
    pluginManager.registerEvents(blockSpreadListener, javaPlugin);
    pluginManager.registerEvents(jobsExpGainListener, javaPlugin);
    pluginManager.registerEvents(jobsPrePaymentListener, javaPlugin);
    log.info("Event listeners registered.");
  }
}
