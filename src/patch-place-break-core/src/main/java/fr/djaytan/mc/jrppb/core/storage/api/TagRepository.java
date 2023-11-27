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
package fr.djaytan.mc.jrppb.core.storage.api;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import fr.djaytan.mc.jrppb.api.entities.Tag;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Tag} repository.
 *
 * <p>A repository represents an abstraction of data storage. This is the appliance of the Data
 * Access Object pattern (DAO).
 *
 * <p><i>Note:</i> It is agnostic about whether the final storage is persistent (e.g. non-temporary
 * files) or not (like in-memory). However, it is expected that the underlying implementation shares
 * ways to select the type of storage wished. At least one type must be persistent. Non-persistent
 * ones are typically reserved for test purposes. If there is no way to configure the type of
 * storage, or no particular type wished as been provided, then the default type must be persistent.
 */
public interface TagRepository {

  /**
   * Puts a tag at the targeted {@link BlockLocation}.
   *
   * <p>If no tag exists at the targeted location, a new resource is created. Otherwise, the
   * existing tag is overridden.
   *
   * @param tag The tag to be put at the targeted location.
   */
  void put(@NotNull Tag tag);

  /**
   * Updates the locations of tags from old to new ones.
   *
   * <p>More concretely, a tag from an old location must overwrite the existing one at the new
   * location. Then, no tag must exist anymore at the old location.
   *
   * @param oldNewLocationPairs The set of old-new location pairs
   */
  void updateLocations(@NotNull OldNewBlockLocationPairSet oldNewLocationPairs);

  /**
   * Finds a tag from a {@link BlockLocation}.
   *
   * @param blockLocation The location where the sought tag may be.
   * @return The tag which matches with the given location if exists.
   */
  @NotNull
  Optional<Tag> findByLocation(@NotNull BlockLocation blockLocation);

  /**
   * Deletes a tag from its {@link BlockLocation}.
   *
   * <p>If no tag exists at the given location, then nothing is done.
   *
   * @param blockLocation The location of the tag to be deleted.
   */
  void delete(@NotNull BlockLocation blockLocation);
}
