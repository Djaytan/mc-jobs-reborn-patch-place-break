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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities;

import lombok.NonNull;
import lombok.Value;

/**
 * An immutable and thread-safe tag location.
 */
// TODO: should use only integer values for coordinates
@Value(staticConstructor = "of")
public class TagLocation {

  @NonNull
  String worldName;
  double x;
  double y;
  double z;

  /**
   * Adjusts this tag location by creating a new one.
   *
   * @param tagVector The adjustment to apply to this tag location.
   * @return The new tag location with the applied adjustment.
   */
  public @NonNull TagLocation adjust(@NonNull TagVector tagVector) {
    double newX = x + tagVector.getModX();
    double newY = y + tagVector.getModY();
    double newZ = z + tagVector.getModZ();
    return TagLocation.of(worldName, newX, newY, newZ);
  }
}
