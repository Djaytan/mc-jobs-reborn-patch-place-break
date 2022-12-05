/*
 * JobsReborn extension to patch place-break (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
