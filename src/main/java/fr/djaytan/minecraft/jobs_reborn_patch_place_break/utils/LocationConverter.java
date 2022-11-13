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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.utils;

import javax.inject.Singleton;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Preconditions;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;

@Singleton
public class LocationConverter {

  public @NotNull TagLocation convert(@NotNull Location location) {
    Preconditions.checkNotNull(location);
    Preconditions.checkNotNull(location.getWorld());

    return new TagLocation(location.getWorld().getName(), location.getX(), location.getY(),
        location.getZ());
  }
}
