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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.guice;

import com.google.inject.AbstractModule;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.dao.PatchPlaceAndBreakTagDao;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.dao.PatchPlaceAndBreakTagSqlDao;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SQLiteDataSource;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SQLiteDataSourceInitializer;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SqlDataSource;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SqlDataSourceInitializer;

public class GuiceGeneralModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SqlDataSource.class).to(SQLiteDataSource.class);
    bind(SqlDataSourceInitializer.class).to(SQLiteDataSourceInitializer.class);
    bind(PatchPlaceAndBreakTagDao.class).to(PatchPlaceAndBreakTagSqlDao.class);
  }
}
