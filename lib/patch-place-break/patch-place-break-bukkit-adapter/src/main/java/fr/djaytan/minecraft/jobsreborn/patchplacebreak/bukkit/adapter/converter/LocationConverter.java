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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import java.util.Objects;

import javax.inject.Singleton;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;

/**
 * Represents a converter between Bukkit {@link Location} and {@link TagLocation}.
 */
@Singleton
public class LocationConverter implements UnidirectionalConverter<Location, TagLocation> {

  /**
   * Converts a Bukkit location to a tag location.
   * <p>
   *  <i>Note: the Bukkit world of the Bukkit location shall never be <code>null</code>.</i>
   * </p>
   *
   * @param convertible The Bukkit location to convert.
   * @return The converted Bukkit location to a tag location.
   * @throws NullPointerException if the Bukkit world of the Bukkit location is <code>null</code>.
   */
  @Override
  public @NotNull TagLocation convert(@NotNull Location convertible) {
    Objects.requireNonNull(convertible.getWorld());

    String worldName = convertible.getWorld().getName();
    double x = convertible.getX();
    double y = convertible.getY();
    double z = convertible.getZ();

    return TagLocation.of(worldName, x, y, z);
  }
}
