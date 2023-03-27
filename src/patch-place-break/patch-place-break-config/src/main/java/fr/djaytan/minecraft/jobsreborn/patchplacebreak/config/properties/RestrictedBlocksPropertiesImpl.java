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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictedBlocksProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictionMode;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Value;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
@Value
public class RestrictedBlocksPropertiesImpl implements Properties, RestrictedBlocksProperties {

  @Required
  @Comment("List of materials used when applying restrictions to patch tags")
  Set<String> materials;

  @NotNull
  @Required
  @Comment(
      "Define the restriction mode when handling tags for the listed blocks."
          + "\nThree values are available:\n"
          + "* BLACKLIST: Only listed blocks are marked as restricted \n"
          + "* WHITELIST: All blocks are marked as restricted except the listed ones\n"
          + "* DISABLED: No restriction applied")
  RestrictionMode restrictionMode;

  public RestrictedBlocksPropertiesImpl() {
    this.materials = new HashSet<>();
    this.restrictionMode = RestrictionMode.DISABLED;
  }

  public RestrictedBlocksPropertiesImpl(
      Collection<String> materials, RestrictionMode restrictionMode) {
    this.materials = Collections.unmodifiableSet(new HashSet<>(materials));
    this.restrictionMode = restrictionMode;
  }

  @Override
  public boolean isRestricted(@org.jetbrains.annotations.NotNull String material) {
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
