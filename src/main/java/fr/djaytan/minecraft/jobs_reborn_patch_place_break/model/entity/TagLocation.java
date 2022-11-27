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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.MoreObjects;

/**
 * An immutable and thread-safe tag location.
 */
public final class TagLocation {

  private final String worldName;
  private final double x;
  private final double y;
  private final double z;

  private TagLocation(@NotNull String worldName, double x, double y, double z) {
    this.worldName = worldName;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public static @NotNull TagLocation of(@NotNull String worldName, double x, double y, double z) {
    return new TagLocation(worldName, x, y, z);
  }

  public @NotNull String getWorldName() {
    return worldName;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TagLocation that = (TagLocation) o;
    return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0
        && Double.compare(that.z, z) == 0 && worldName.equals(that.worldName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(worldName, x, y, z);
  }

  @Override
  public @NotNull String toString() {
    return MoreObjects.toStringHelper(this).add("worldName", worldName).add("x", x).add("y", y)
        .add("z", z).toString();
  }
}
