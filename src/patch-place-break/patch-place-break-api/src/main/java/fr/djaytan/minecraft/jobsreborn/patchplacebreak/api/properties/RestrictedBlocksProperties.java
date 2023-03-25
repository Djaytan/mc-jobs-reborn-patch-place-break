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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the properties related to block restrictions when handling tags.
 *
 * <p>You can check if a block should be ignored according to the current properties thanks to the
 * {@link #isRestricted(String)} method.
 */
@Value
public class RestrictedBlocksProperties {

  Set<String> materials;
  RestrictionMode restrictionMode;

  public RestrictedBlocksProperties(
      @NotNull Set<String> materials, @NotNull RestrictionMode restrictionMode) {
    this.materials = Collections.unmodifiableSet(new HashSet<>(materials));
    this.restrictionMode = restrictionMode;
  }

  /**
   * Checks if a given material is restricted based on defined properties.
   *
   * @param material The material name to check.
   * @return true if the material is restricted, false otherwise.
   */
  public boolean isRestricted(@NotNull String material) {
    switch (restrictionMode) {
      case BLACKLIST:
        return materials.contains(material);
      case WHITELIST:
        return !materials.contains(material);
      case DISABLED:
        return false;
      default:
        throw new IllegalStateException(
            String.format("Restriction mode %s is not supported.", restrictionMode));
    }
  }
}
