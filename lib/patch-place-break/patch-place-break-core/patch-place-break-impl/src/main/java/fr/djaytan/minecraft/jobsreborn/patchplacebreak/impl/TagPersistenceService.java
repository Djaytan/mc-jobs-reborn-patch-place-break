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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.TagDao;
import lombok.NonNull;

@Singleton
public class TagPersistenceService {

  private final TagDao tagDao;

  @Inject
  public TagPersistenceService(TagDao tagDao) {
    this.tagDao = tagDao;
  }

  public @NonNull CompletableFuture<Void> persistTag(boolean isEphemeral,
      @NonNull TagLocation tagLocation) {
    return CompletableFuture.runAsync(() -> {
      UUID tagUuid = UUID.randomUUID();
      LocalDateTime localDateTime = LocalDateTime.now();
      Tag tag = Tag.of(tagUuid, localDateTime, isEphemeral, tagLocation);
      tagDao.put(tag);
    });
  }

  public @NonNull CompletableFuture<Optional<Tag>> findTagByLocation(
      @NonNull TagLocation tagLocation) {
    return CompletableFuture.supplyAsync(() -> tagDao.findByLocation(tagLocation));
  }

  public @NonNull CompletableFuture<Void> removeTag(@NonNull TagLocation tagLocation) {
    return CompletableFuture.runAsync(() -> tagDao.delete(tagLocation));
  }
}
