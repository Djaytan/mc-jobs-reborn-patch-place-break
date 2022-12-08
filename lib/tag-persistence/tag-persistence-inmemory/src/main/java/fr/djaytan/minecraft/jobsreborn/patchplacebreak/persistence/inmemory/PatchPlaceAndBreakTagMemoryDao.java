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
