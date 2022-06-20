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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.util.concurrent.Executor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/** Guice module for Bukkit plugin. */
public class GuiceBukkitModule extends AbstractModule {

  private final JavaPlugin plugin;

  /**
   * Constructor.
   *
   * @param plugin The Bukkit plugin.
   */
  public GuiceBukkitModule(@NotNull JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  public @NotNull Plugin providePlugin() {
    return plugin;
  }

  @Provides
  @Singleton
  public @NotNull JavaPlugin provideJavaPlugin() {
    return plugin;
  }

  @Provides
  @Singleton
  public @NotNull Logger provideLogger() {
    return plugin.getSLF4JLogger();
  }

  @Provides
  @Singleton
  public @NotNull PluginManager providePluginManager() {
    return plugin.getServer().getPluginManager();
  }

  @Provides
  @Singleton
  public @NotNull ServicesManager provideServicesManager() {
    return plugin.getServer().getServicesManager();
  }

  @Provides
  @Singleton
  public @NotNull Server provideServer() {
    return plugin.getServer();
  }

  @Provides
  @Singleton
  public @NotNull ItemFactory provideItemFactory() {
    return plugin.getServer().getItemFactory();
  }

  @Provides
  @Singleton
  public @NotNull ConsoleCommandSender provideConsoleCommandSender() {
    return plugin.getServer().getConsoleSender();
  }

  @Provides
  @Singleton
  public @NotNull BukkitScheduler provideBukkitScheduler() {
    return plugin.getServer().getScheduler();
  }

  @Provides
  @Singleton
  public @NotNull ScoreboardManager provideScoreboardManager() {
    return plugin.getServer().getScoreboardManager();
  }

  @Provides
  @Singleton
  public @NotNull StructureManager provideStructureManager() {
    return plugin.getServer().getStructureManager();
  }

  @Provides
  @Singleton
  public @NotNull Executor provideMainThreadExecutor() {
    return runnable -> plugin.getServer().getScheduler().runTask(plugin, runnable);
  }
}
