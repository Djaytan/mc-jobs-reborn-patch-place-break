/*-
 * #%L
 * JobsReborn-PatchPlaceBreak
 * %%
 * Copyright (C) 2022 - 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 * %%
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
 * #L%
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities;

import lombok.NonNull;
import lombok.Value;

/** An immutable and thread-safe block location. */
@Value(staticConstructor = "of")
public class BlockLocation {

  @NonNull String worldName;
  int x;
  int y;
  int z;

  /**
   * Creates a block location from a specified one and a given direction.
   *
   * @param blockLocation The original block location from which to create the new one.
   * @param direction The direction permitting to determine the new block location.
   * @return A new block location from a specified one and a given direction.
   */
  public static @NonNull BlockLocation from(
      @NonNull BlockLocation blockLocation, @NonNull Vector direction) {
    int newX = blockLocation.getX() + direction.getModX();
    int newY = blockLocation.getY() + direction.getModY();
    int newZ = blockLocation.getZ() + direction.getModZ();
    return BlockLocation.of(blockLocation.getWorldName(), newX, newY, newZ);
  }
}
