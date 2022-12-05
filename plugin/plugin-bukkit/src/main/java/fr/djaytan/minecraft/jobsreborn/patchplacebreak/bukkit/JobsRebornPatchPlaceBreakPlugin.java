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
 *
 * @author Djaytan
 * @see JavaPlugin
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
    }

    logger.info("The plugin data folder doesn't exists yet: creating it.");
    Files.createDirectory(dataFolder);
  }
}
