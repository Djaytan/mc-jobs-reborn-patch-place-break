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
package fr.djaytan.mc.jrppb.core.config.properties;

import fr.djaytan.mc.jrppb.api.properties.RestrictedBlocksProperties;
import fr.djaytan.mc.jrppb.api.properties.RestrictionMode;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public final class RestrictedBlocksPropertiesImpl
    implements Properties, RestrictedBlocksProperties {

  @Required
  @Comment("List of materials used when applying restrictions to patch tags")
  private final Set<String> materials;

  @NotNull
  @Required
  @Comment(
      """
          Define the restriction mode when handling tags for the listed blocks.
          Three values are available:
          * BLACKLIST: Only listed blocks are marked as restricted
          * WHITELIST: All blocks are marked as restricted except the listed ones
          * DISABLED: No restriction applied""")
  private final RestrictionMode restrictionMode;

  public RestrictedBlocksPropertiesImpl() {
    this.materials = new HashSet<>();
    this.restrictionMode = RestrictionMode.DISABLED;
  }

  /** Testing purposes only. */
  public RestrictedBlocksPropertiesImpl(
      @org.jetbrains.annotations.NotNull Collection<String> materials,
      @Nullable RestrictionMode restrictionMode) {
    this.materials = Set.copyOf(materials);
    this.restrictionMode = restrictionMode;
  }

  @org.jetbrains.annotations.NotNull
  Set<String> getMaterials() {
    return materials;
  }

  @org.jetbrains.annotations.NotNull
  RestrictionMode getRestrictionMode() {
    return Objects.requireNonNull(restrictionMode);
  }

  @Override
  public boolean isRestricted(@org.jetbrains.annotations.NotNull String material) {
    return switch (restrictionMode) {
      case BLACKLIST -> materials.contains(material);
      case WHITELIST -> !materials.contains(material);
      case DISABLED -> false;
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RestrictedBlocksPropertiesImpl that = (RestrictedBlocksPropertiesImpl) o;
    return Objects.equals(materials, that.materials) && restrictionMode == that.restrictionMode;
  }

  @Override
  public int hashCode() {
    return Objects.hash(materials, restrictionMode);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", RestrictedBlocksPropertiesImpl.class.getSimpleName() + "[", "]")
        .add("materials=" + materials)
        .add("restrictionMode=" + restrictionMode)
        .toString();
  }
}
