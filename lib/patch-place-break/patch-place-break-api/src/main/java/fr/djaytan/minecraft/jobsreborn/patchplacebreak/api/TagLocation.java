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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import org.jetbrains.annotations.NotNull;

import lombok.Value;

/**
 * An immutable and thread-safe tag location.
 */
@Value(staticConstructor = "of")
public class TagLocation {

  String worldName;
  double x;
  double y;
  double z;

  /**
   * Adjusts this tag location by creating a new one.
   *
   * @param tagVector The adjustment to apply to this tag location.
   * @return The new tag location with the applied adjustment.
   */
  public @NotNull TagLocation adjust(TagVector tagVector) {
    double newX = x + tagVector.getModX();
    double newY = y + tagVector.getModY();
    double newZ = z + tagVector.getModZ();
    return TagLocation.of(worldName, newX, newY, newZ);
  }
}
