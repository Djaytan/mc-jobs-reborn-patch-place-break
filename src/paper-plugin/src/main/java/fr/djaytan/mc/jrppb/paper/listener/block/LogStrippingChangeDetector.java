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
package fr.djaytan.mc.jrppb.paper.listener.block;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/** Utility for detecting log stripping changes. */
public final class LogStrippingChangeDetector {

  public static boolean isLogStrippingChange(
      @NotNull Material initialBlockType, @NotNull Material finalBlockType) {
    return isNotStrippedLog(initialBlockType) && isStrippedLog(finalBlockType);
  }

  public static boolean isLogStrippingChange(
      @NotNull ItemStack itemInPlayerHand, @NotNull Material finalBlockType) {
    return isStrippedLog(finalBlockType) && Tag.ITEMS_AXES.isTagged(itemInPlayerHand.getType());
  }

  private static boolean isNotStrippedLog(@NotNull Material material) {
    return isLog(material) && !isStripped(material);
  }

  private static boolean isStrippedLog(@NotNull Material material) {
    return isLog(material) && isStripped(material);
  }

  private static boolean isLog(@NotNull Material material) {
    return Tag.LOGS.isTagged(material);
  }

  /**
   * @implNote We are resigned to relying on {@link Material} name since the Spigot API doesn't
   *     expose any better and supported way to perform such check. Additionally, Minecraft doesn't
   *     provide a proper tag for identifying stripped logs. This is why we can't have absolute
   *     confidence about the implementation's compatibility with all future Spigot versions. The
   *     coupling is limited to the bare minimum for maximum compatibility.
   */
  private static boolean isStripped(Material log) {
    return log.name().contains("STRIPPED");
  }
}
