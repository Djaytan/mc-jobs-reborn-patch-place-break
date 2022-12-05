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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.inmemory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.PatchPlaceAndBreakTagDao;

@Singleton
public class PatchPlaceAndBreakTagMemoryDao implements PatchPlaceAndBreakTagDao {

  private final Map<UUID, Tag> patchPlaceAndBreakTagMap = new HashMap<>();

  @Override
  public void put(@NotNull Tag tag) {
    delete(tag.getTagLocation());
    patchPlaceAndBreakTagMap.put(tag.getUuid(), tag);
  }

  @Override
  public @NotNull Optional<Tag> findByLocation(@NotNull TagLocation tagLocation) {
    for (Tag tag : patchPlaceAndBreakTagMap.values()) {
      if (tag.getTagLocation().equals(tagLocation)) {
        return Optional.of(tag);
      }
    }
    return Optional.empty();
  }

  @Override
  public void delete(@NotNull TagLocation tagLocation) {
    Optional<Tag> patchPlaceAndBreakTag = findByLocation(tagLocation);
    patchPlaceAndBreakTag
        .ifPresent(placeAndBreakTag -> patchPlaceAndBreakTagMap.remove(placeAndBreakTag.getUuid()));
  }
}
