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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.PatchPlaceAndBreakTag;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;

@Singleton
public class PatchPlaceAndBreakTagMemoryDao implements PatchPlaceAndBreakTagDao {

  private final Map<UUID, PatchPlaceAndBreakTag> patchPlaceAndBreakTagMap = new HashMap<>();

  @Override
  public void put(@NotNull PatchPlaceAndBreakTag patchPlaceAndBreakTag) {
    delete(patchPlaceAndBreakTag.getTagLocation());
    patchPlaceAndBreakTagMap.put(patchPlaceAndBreakTag.getUuid(), patchPlaceAndBreakTag);
  }

  @Override
  public @NotNull Optional<PatchPlaceAndBreakTag> findByLocation(@NotNull TagLocation tagLocation) {
    for (PatchPlaceAndBreakTag patchPlaceAndBreakTag : patchPlaceAndBreakTagMap.values()) {
      if (patchPlaceAndBreakTag.getTagLocation().equals(tagLocation)) {
        return Optional.of(patchPlaceAndBreakTag);
      }
    }
    return Optional.empty();
  }

  @Override
  public void delete(@NotNull TagLocation tagLocation) {
    Optional<PatchPlaceAndBreakTag> patchPlaceAndBreakTag = findByLocation(tagLocation);
    patchPlaceAndBreakTag
        .ifPresent(placeAndBreakTag -> patchPlaceAndBreakTagMap.remove(placeAndBreakTag.getUuid()));
  }
}
