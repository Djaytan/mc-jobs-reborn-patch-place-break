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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.ListenerRegister;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.MetricsFacade;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakCore;
import java.time.Clock;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/** Represents factory of the main dependencies. */
public final class JobsRebornPatchPlaceBreakFactory {

  private final Injector injector;

  public JobsRebornPatchPlaceBreakFactory(@NotNull JavaPlugin javaPlugin, @NotNull Clock clock) {
    this.injector = createInjector(javaPlugin, clock);
  }

  private static @NotNull Injector createInjector(
      @NotNull JavaPlugin javaPlugin, @NotNull Clock clock) {
    return Guice.createInjector(
        new BukkitModule(javaPlugin), new JobsRebornPatchPlaceBreakModule(clock));
  }

  public @NotNull ListenerRegister listenerRegister() {
    return injector.getInstance(ListenerRegister.class);
  }

  public @NotNull MetricsFacade metricsFacade() {
    return injector.getInstance(MetricsFacade.class);
  }

  public @NotNull PatchPlaceBreakCore patchPlaceBreakCore() {
    return injector.getInstance(PatchPlaceBreakCore.class);
  }

  public @NotNull PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi() {
    return injector.getInstance(PatchPlaceBreakBukkitAdapterApi.class);
  }
}
