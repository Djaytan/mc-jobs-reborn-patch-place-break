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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import lombok.NonNull;

public class GuiceBukkitModule extends AbstractModule {

  private final JavaPlugin javaPlugin;

  public GuiceBukkitModule(@NonNull JavaPlugin javaPlugin) {
    this.javaPlugin = javaPlugin;
  }

  @Provides
  @Singleton
  public @NonNull JavaPlugin provideJavaPlugin() {
    return javaPlugin;
  }

  @Provides
  @Singleton
  public @NonNull Logger provideSlf4jLogger() {
    // The logger name doesn't matter
    return LoggerFactory.getLogger("");
  }

  @Provides
  @Singleton
  public @NonNull Server provideServer() {
    return javaPlugin.getServer();
  }

  @Provides
  @Singleton
  public @NonNull PluginManager providePluginManager(@NonNull Server server) {
    return server.getPluginManager();
  }

  @Provides
  @Singleton
  public @NonNull ServicesManager provideServicesManager(@NonNull Server server) {
    return server.getServicesManager();
  }

  @Provides
  @Singleton
  public @NonNull ItemFactory provideItemFactory(@NonNull Server server) {
    return server.getItemFactory();
  }

  @Provides
  @Singleton
  public @NonNull ConsoleCommandSender provideConsoleCommandSender(@NonNull Server server) {
    return server.getConsoleSender();
  }

  @Provides
  @Singleton
  public @NonNull BukkitScheduler provideBukkitScheduler(@NonNull Server server) {
    return server.getScheduler();
  }

  @Provides
  @Named("dataFolder")
  @Singleton
  public @NonNull Path provideDataFolderPath() {
    return javaPlugin.getDataFolder().toPath();
  }

  @Provides
  @Singleton
  public @NonNull Executor provideMainThreadExecutor(@NonNull BukkitScheduler bukkitScheduler) {
    return runnable -> bukkitScheduler.runTask(javaPlugin, runnable);
  }
}