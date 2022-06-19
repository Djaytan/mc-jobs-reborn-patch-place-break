/*
 * Blacklisting enchantments plugin for Minecraft (Bukkit servers)
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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

/** This class represents a JobsReborn patch place-break plugin. */
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  @Inject private ListenerRegister listenerRegister;

  @Override
  public void onEnable() {
    Injector injector = Guice.createInjector(new GuiceBukkitModule(this), new GuicePluginModule());
    injector.injectMembers(this);

    listenerRegister.registerListeners();
  }
}
