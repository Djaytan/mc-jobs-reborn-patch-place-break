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

import fr.djaytan.mc.jrppb.core.RestrictedBlocksProperties;
import fr.djaytan.mc.jrppb.core.RestrictionMode;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record RestrictedBlocksConfigProperties(
    @Required @Comment(MATERIALS_COMMENT) @NotNull Set<String> materials,
    @Required @Comment(RESTRICTION_MODE_COMMENT) @NotNull RestrictionMode restrictionMode)
    implements ConfigProperties {

  private static final String MATERIALS_COMMENT =
      "List of materials used when applying restrictions to patch tags";
  private static final String RESTRICTION_MODE_COMMENT =
      """
      Define the restriction mode when handling tags for the listed blocks.
      Three values are available:
      * BLACKLIST: Only listed blocks are marked as restricted
      * WHITELIST: All blocks are marked as restricted except the listed ones
      * DISABLED: No restriction applied""";

  public static final RestrictedBlocksConfigProperties DEFAULT =
      fromModel(RestrictedBlocksProperties.DEFAULT);

  public static @NotNull RestrictedBlocksConfigProperties fromModel(
      @NotNull RestrictedBlocksProperties model) {
    return new RestrictedBlocksConfigProperties(model.materials(), model.restrictionMode());
  }

  public @NotNull RestrictedBlocksProperties toModel() {
    return new RestrictedBlocksProperties(materials, restrictionMode);
  }
}
