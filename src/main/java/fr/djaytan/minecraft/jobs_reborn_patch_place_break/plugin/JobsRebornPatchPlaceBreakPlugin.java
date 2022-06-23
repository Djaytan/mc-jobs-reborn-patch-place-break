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

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsController;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsControllerImpl;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.bukkit.BlockBreakListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.bukkit.BlockGrowListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.bukkit.BlockPistonListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.bukkit.BlockPlaceListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.bukkit.BlockSpreadListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.jobs.JobsExpGainListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.jobs.JobsExpGainVerificationListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.jobs.JobsPrePaymentListener;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.listener.jobs.JobsPrePaymentVerificationListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class represents a JobsReborn patch place-break plugin.
 *
 * @author Djaytan
 * @see JavaPlugin
 */
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    PatchPlaceAndBreakJobsController patchPlaceAndBreakJobsController =
        new PatchPlaceAndBreakJobsControllerImpl(
            getServer().getScheduler(), getSLF4JLogger(), this);
    ListenerRegister listenerRegister =
        new ListenerRegister(
            this,
            this.getServer().getPluginManager(),
            new BlockBreakListener(patchPlaceAndBreakJobsController),
            new BlockGrowListener(patchPlaceAndBreakJobsController),
            new BlockPistonListener(patchPlaceAndBreakJobsController),
            new BlockPlaceListener(patchPlaceAndBreakJobsController),
            new BlockSpreadListener(patchPlaceAndBreakJobsController),
            new JobsExpGainListener(patchPlaceAndBreakJobsController),
            new JobsExpGainVerificationListener(patchPlaceAndBreakJobsController),
            new JobsPrePaymentListener(patchPlaceAndBreakJobsController),
            new JobsPrePaymentVerificationListener(patchPlaceAndBreakJobsController));
    listenerRegister.registerListeners();
  }
}
