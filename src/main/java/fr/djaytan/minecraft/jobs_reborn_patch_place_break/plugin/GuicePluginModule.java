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
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.JobsController;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.JobsControllerImpl;

/** General Guice module. */
public class GuicePluginModule extends AbstractModule {

  @Override
  public void configure() {
    bind(JobsController.class).to(JobsControllerImpl.class);
  }
}
