/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockBreakListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockGrowListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockPistonListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockPlaceListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockSpreadListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsExpGainListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsExpGainVerificationListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsPrePaymentListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsPrePaymentVerificationListener;

/**
 * This class represents a register of listeners.
 *
 * @author Djaytan
 */
@Singleton
public class ListenerRegister {

  private final Plugin plugin;
  private final PluginManager pluginManager;

  private final BlockBreakListener blockBreakListener;
  private final BlockGrowListener blockGrowListener;
  private final BlockPistonListener blockPistonListener;
  private final BlockPlaceListener blockPlaceListener;
  private final BlockSpreadListener blockSpreadListener;
  private final JobsExpGainListener jobsExpGainListener;
  private final JobsExpGainVerificationListener jobsExpGainVerificationListener;
  private final JobsPrePaymentListener jobsPrePaymentListener;
  private final JobsPrePaymentVerificationListener jobsPrePaymentVerificationListener;

  @Inject
  public ListenerRegister(@NotNull Plugin plugin, @NotNull PluginManager pluginManager,
      @NotNull BlockBreakListener blockBreakListener, @NotNull BlockGrowListener blockGrowListener,
      @NotNull BlockPistonListener blockPistonListener,
      @NotNull BlockPlaceListener blockPlaceListener,
      @NotNull BlockSpreadListener blockSpreadListener,
      @NotNull JobsExpGainListener jobsExpGainListener,
      @NotNull JobsExpGainVerificationListener jobsExpGainVerificationListener,
      @NotNull JobsPrePaymentListener jobsPrePaymentListener,
      @NotNull JobsPrePaymentVerificationListener jobsPrePaymentVerificationListener) {
    this.plugin = plugin;
    this.pluginManager = pluginManager;

    this.blockBreakListener = blockBreakListener;
    this.blockGrowListener = blockGrowListener;
    this.blockPistonListener = blockPistonListener;
    this.blockPlaceListener = blockPlaceListener;
    this.blockSpreadListener = blockSpreadListener;
    this.jobsExpGainListener = jobsExpGainListener;
    this.jobsExpGainVerificationListener = jobsExpGainVerificationListener;
    this.jobsPrePaymentListener = jobsPrePaymentListener;
    this.jobsPrePaymentVerificationListener = jobsPrePaymentVerificationListener;
  }

  /**
   * The purposes of this method is simple: registering listeners through the {@link PluginManager}.
   */
  public void registerListeners() {
    pluginManager.registerEvents(blockBreakListener, plugin);
    pluginManager.registerEvents(blockGrowListener, plugin);
    pluginManager.registerEvents(blockPistonListener, plugin);
    pluginManager.registerEvents(blockPlaceListener, plugin);
    pluginManager.registerEvents(blockSpreadListener, plugin);
    pluginManager.registerEvents(jobsExpGainListener, plugin);
    pluginManager.registerEvents(jobsExpGainVerificationListener, plugin);
    pluginManager.registerEvents(jobsPrePaymentListener, plugin);
    pluginManager.registerEvents(jobsPrePaymentVerificationListener, plugin);
  }
}