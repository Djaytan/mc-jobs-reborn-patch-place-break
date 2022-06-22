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

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.JobsController;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.JobsControllerImpl;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.BlockGrowListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.BlockPlaceListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.JobsExpGainListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.JobsPrePaymentListener;
import org.bukkit.plugin.java.JavaPlugin;

/** This class represents a JobsReborn patch place-break plugin. */
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    JobsController jobsController = new JobsControllerImpl(this);
    ListenerRegister listenerRegister =
        new ListenerRegister(
            this,
            this.getServer().getPluginManager(),
            new BlockGrowListener(jobsController),
            new BlockPlaceListener(jobsController),
            new JobsExpGainListener(jobsController),
            new JobsPrePaymentListener(jobsController));
    listenerRegister.registerListeners();
  }
}
