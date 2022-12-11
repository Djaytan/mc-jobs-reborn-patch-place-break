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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.bukkit.plugin.java.JavaPlugin;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.inject.GuiceBukkitInjector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.DataSource;
import lombok.SneakyThrows;

/**
 * This class represents a JobsReborn patch place-break plugin.
 */
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  @Inject
  private DataSource dataSource;
  @Inject
  private ListenerRegister listenerRegister;
  @Inject
  private Logger logger;
  @Inject
  private MetricsFacade metricsFacade;

  @Override
  public void onEnable() {
    getLogger().info("Guice injection");
    GuiceBukkitInjector.inject(this, getLogger());

    logger.info("Preparing plugin's data folder");
    preparePluginDataFolder();

    logger.info("Connection to the database");
    dataSource.connect();

    logger.info("Event listeners registration");
    listenerRegister.registerListeners();

    logger.info("Activation of metrics collection");
    metricsFacade.activateMetricsCollection();

    logger.info("JobsReborn-PatchPlaceBreak successfully enabled.");
  }

  @Override
  public void onDisable() {
    getLogger().info("Disconnection from the SQLite database");
    dataSource.disconnect();
  }

  @SneakyThrows
  private void preparePluginDataFolder() {
    Path dataFolder = getDataFolder().toPath();

    if (Files.exists(dataFolder)) {
      logger.info("The plugin data folder already exists: skipping its creation.");
      return;
    }

    logger.info("The plugin data folder doesn't exists yet: creating it.");
    Files.createDirectory(dataFolder);
  }
}
