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

import javax.inject.Inject;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SqlDataSource;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.guice.GuiceInjector;

/**
 * This class represents a JobsReborn patch place-break plugin.
 *
 * @author Djaytan
 * @see JavaPlugin
 */
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  private static final int BSTATS_ID = 16899;

  @Inject
  private ListenerRegister listenerRegister;
  @Inject
  private SqlDataSource sqlDataSource;
  @Inject
  private SqlDataSourceInitializer sqlDataSourceInitializer;

  @Override
  public void onEnable() {
    getLogger().info("Guice injection");
    GuiceInjector.inject(this);

    getLogger().info("SQL database initialization");
    sqlDataSourceInitializer.initialize();

    getLogger().info("Database connection");
    sqlDataSource.connect();

    getLogger().info("Creating default SQL table...");
    boolean tablesCreated = sqlDataSourceInitializer.createTablesIfNotExists();
    if (tablesCreated) {
      getLogger().info("SQL table created");
    } else {
      getLogger().info("SQL table already exists, skipping");
    }

    getLogger().info("Event listeners registration");
    listenerRegister.registerListeners();

    getLogger().info("Activation of metrics collection with bStats");
    activateMetricsCollection();

    getLogger().info("JobsReborn-PatchPlaceBreak successfully enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info("Disconnecting database...");
    sqlDataSource.disconnect();
  }

  private void activateMetricsCollection() {
    new Metrics(this, BSTATS_ID);
  }
}
