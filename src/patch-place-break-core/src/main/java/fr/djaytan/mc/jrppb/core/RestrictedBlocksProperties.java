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
package fr.djaytan.mc.jrppb.core;

import static java.util.Collections.emptySet;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the properties related to block restrictions when handling tags.
 *
 * <p>You can check if a block should be ignored according to the current properties thanks to the
 * {@link #isRestricted(String)} method.
 */
public record RestrictedBlocksProperties(
    @NotNull Set<String> materials, @NotNull RestrictionMode restrictionMode) {

  public static final RestrictedBlocksProperties DEFAULT =
      new RestrictedBlocksProperties(emptySet(), RestrictionMode.DISABLED);

  /**
   * Checks if a given material is restricted based on defined properties.
   *
   * @param material The material name to check.
   * @return true if the material is restricted, false otherwise.
   */
  public boolean isRestricted(@NotNull String material) {
    return switch (restrictionMode) {
      case BLACKLIST -> materials.contains(material);
      case WHITELIST -> !materials.contains(material);
      case DISABLED -> false;
    };
  }
}
