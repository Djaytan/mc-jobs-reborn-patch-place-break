/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;

/** Represents a set of {@link OldNewBlockLocationPair}. */
@Value
public class OldNewBlockLocationPairSet {

  Set<OldNewBlockLocationPair> oldNewBlockLocationPairs;

  public OldNewBlockLocationPairSet(
      @NonNull Collection<OldNewBlockLocationPair> oldNewBlockLocationPairs) {
    this.oldNewBlockLocationPairs =
        Collections.unmodifiableSet(new HashSet<>(oldNewBlockLocationPairs));
  }

  /**
   * Flatten old and new locations to retrieve all of them into a simple set.
   *
   * @return A set containing flattened old and new locations.
   */
  public @NonNull Set<BlockLocation> flattenBlockLocations() {
    return oldNewBlockLocationPairs.stream()
        .flatMap(
            oldNewBlockLocationPair ->
                Stream.of(
                    oldNewBlockLocationPair.getOldBlockLocation(),
                    oldNewBlockLocationPair.getNewBlockLocation()))
        .collect(Collectors.toSet());
  }
}
