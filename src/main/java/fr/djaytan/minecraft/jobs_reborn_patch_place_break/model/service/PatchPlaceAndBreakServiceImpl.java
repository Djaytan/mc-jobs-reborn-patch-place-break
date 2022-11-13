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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.dao.PatchPlaceAndBreakTagDao;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.PatchPlaceAndBreakTag;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;

@Singleton
public class PatchPlaceAndBreakServiceImpl implements PatchPlaceAndBreakService {

  private final PatchPlaceAndBreakTagDao patchPlaceAndBreakTagDao;

  @Inject
  public PatchPlaceAndBreakServiceImpl(@NotNull PatchPlaceAndBreakTagDao patchPlaceAndBreakTagDao) {
    this.patchPlaceAndBreakTagDao = patchPlaceAndBreakTagDao;
  }

  @Override
  public @NotNull CompletableFuture<Void> putTag(boolean isEphemeral,
      @NotNull TagLocation tagLocation) {
    return CompletableFuture.runAsync(() -> {
      UUID tagUuid = UUID.randomUUID();
      LocalDateTime localDateTime = LocalDateTime.now();
      PatchPlaceAndBreakTag patchPlaceAndBreakTag =
          new PatchPlaceAndBreakTag(tagUuid, localDateTime, isEphemeral, tagLocation);
      patchPlaceAndBreakTagDao.put(patchPlaceAndBreakTag);
    });
  }

  @Override
  public @NotNull CompletableFuture<Optional<PatchPlaceAndBreakTag>> findTagByLocation(
      @NotNull TagLocation tagLocation) {
    return CompletableFuture
        .supplyAsync(() -> patchPlaceAndBreakTagDao.findByLocation(tagLocation));
  }

  @Override
  public @NotNull CompletableFuture<Void> removeTag(@NotNull TagLocation tagLocation) {
    return CompletableFuture.runAsync(() -> patchPlaceAndBreakTagDao.delete(tagLocation));
  }
}
