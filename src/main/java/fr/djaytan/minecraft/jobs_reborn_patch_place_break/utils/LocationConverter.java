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

import java.util.Objects;

import javax.inject.Singleton;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;

/**
 * Represents a converter between Bukkit {@link Location} and {@link TagLocation}.
 */
@Singleton
public class LocationConverter {

  /**
   * Converts a Bukkit location to a tag location.
   * <p>
   *  <i>Note: the Bukkit world of the Bukkit location shall never be <code>null</code>.</i>
   * </p>
   *
   * @param location The Bukkit location to convert.
   * @return The converted Bukkit location to a tag location.
   * @throws NullPointerException if the Bukkit world of the Bukkit location is <code>null</code>.
   */
  public @NotNull TagLocation convert(@NotNull Location location) {
    Objects.requireNonNull(location.getWorld());

    return TagLocation.of(location.getWorld().getName(), location.getX(), location.getY(),
        location.getZ());
  }
}
