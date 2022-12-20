/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import javax.inject.Singleton;

import org.bukkit.Location;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import lombok.NonNull;

/**
 * Represents a converter between Bukkit {@link Location} and {@link TagLocation}.
 */
@Singleton
public class LocationConverter implements UnidirectionalConverter<Location, TagLocation> {

  LocationConverter() {}

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
  public @NonNull TagLocation convert(@NonNull Location convertible) {
    String worldName = convertible.getWorld().getName();
    double x = convertible.getX();
    double y = convertible.getY();
    double z = convertible.getZ();
    return TagLocation.of(worldName, x, y, z);
  }
}
