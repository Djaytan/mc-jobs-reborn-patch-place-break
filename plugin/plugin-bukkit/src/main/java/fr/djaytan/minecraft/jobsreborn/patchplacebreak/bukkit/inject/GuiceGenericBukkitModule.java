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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.inject;

import java.nio.file.Path;
import java.util.concurrent.Executor;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class GuiceGenericBukkitModule extends AbstractModule {

  private final JavaPlugin plugin;

  public GuiceGenericBukkitModule(@NotNull JavaPlugin plugin) {
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
  @Named("dataFolder")
  @Singleton
  public @NotNull Path provideDataFolderPath() {
    return plugin.getDataFolder().toPath();
  }

  @Provides
  @Singleton
  public @NotNull Executor provideMainThreadExecutor() {
    return runnable -> plugin.getServer().getScheduler().runTask(plugin, runnable);
  }
}
