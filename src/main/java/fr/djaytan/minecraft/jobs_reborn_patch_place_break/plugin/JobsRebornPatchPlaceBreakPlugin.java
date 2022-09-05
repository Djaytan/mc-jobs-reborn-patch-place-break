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

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SqlDataSource;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.guice.GuiceInjector;
import javax.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class represents a JobsReborn patch place-break plugin.
 *
 * @author Djaytan
 * @see JavaPlugin
 */
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  @Inject private ListenerRegister listenerRegister;
  @Inject private SqlDataSource sqlDataSource;
  @Inject private SqlDataSourceInitializer sqlDataSourceInitializer;

  @Override
  public void onEnable() {
    getSLF4JLogger().info("Guice injection");
    GuiceInjector.inject(this);

    getSLF4JLogger().info("SQL database initialization");
    sqlDataSourceInitializer.initialize();

    getSLF4JLogger().info("Database connection");
    sqlDataSource.connect();

    getSLF4JLogger().info("Creating default SQL table...");
    boolean tablesCreated = sqlDataSourceInitializer.createTablesIfNotExists();
    if (tablesCreated) {
      getSLF4JLogger().info("SQL table created.");
    } else {
      getSLF4JLogger().info("SQL table already exists, skipping.");
    }

    getSLF4JLogger().info("Event listeners registration");
    listenerRegister.registerListeners();

    getSLF4JLogger().info("JobsReborn-PatchPlaceBreak successfully enabled.");
  }

  @Override
  public void onDisable() {
    getSLF4JLogger().info("Disconnecting database...");
    sqlDataSource.disconnect();
  }
}
