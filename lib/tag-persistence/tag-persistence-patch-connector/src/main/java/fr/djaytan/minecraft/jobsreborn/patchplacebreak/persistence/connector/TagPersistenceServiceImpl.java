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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.connector;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagPersistenceService;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.PatchPlaceAndBreakTagDao;

@Singleton
public class TagPersistenceServiceImpl implements TagPersistenceService {

  private final PatchPlaceAndBreakTagDao patchPlaceAndBreakTagDao;

  @Inject
  public TagPersistenceServiceImpl(@NotNull PatchPlaceAndBreakTagDao patchPlaceAndBreakTagDao) {
    this.patchPlaceAndBreakTagDao = patchPlaceAndBreakTagDao;
  }

  public @NotNull CompletableFuture<Void> persistTag(boolean isEphemeral,
      @NotNull TagLocation tagLocation) {
    return CompletableFuture.runAsync(() -> {
      UUID tagUuid = UUID.randomUUID();
      LocalDateTime localDateTime = LocalDateTime.now();
      Tag tag = Tag.of(tagUuid, localDateTime, isEphemeral, tagLocation);
      patchPlaceAndBreakTagDao.put(tag);
    });
  }

  public @NotNull CompletableFuture<Optional<Tag>> findTagByLocation(
      @NotNull TagLocation tagLocation) {
    return CompletableFuture
        .supplyAsync(() -> patchPlaceAndBreakTagDao.findByLocation(tagLocation));
  }

  public @NotNull CompletableFuture<Void> removeTag(@NotNull TagLocation tagLocation) {
    return CompletableFuture.runAsync(() -> patchPlaceAndBreakTagDao.delete(tagLocation));
  }
}
