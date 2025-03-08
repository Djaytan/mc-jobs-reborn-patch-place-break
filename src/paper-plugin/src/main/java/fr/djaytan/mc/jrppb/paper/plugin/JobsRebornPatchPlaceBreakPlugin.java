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
package fr.djaytan.mc.jrppb.paper.plugin;

import com.djaytan.bukkit.slf4j.api.BukkitLoggerFactory;
import fr.djaytan.mc.jrppb.core.PatchPlaceBreakCore;
import fr.djaytan.mc.jrppb.paper.adapter.PatchPlaceBreakPaperAdapterApi;
import fr.djaytan.mc.jrppb.paper.listener.ListenerRegister;
import fr.djaytan.mc.jrppb.paper.plugin.inject.JobsRebornPatchPlaceBreakInjector;
import java.time.Clock;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

/** This class represents a JobsReborn patch place-break plugin. */
@SuppressWarnings("unused") // Instantiated by Bukkit's implementation (i.e. CraftBukkit)
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  private final Clock clock;
  private PatchPlaceBreakPaperAdapterApi patchPlaceBreakPaperAdapterApi;
  private PatchPlaceBreakCore patchPlaceBreakCore;

  public JobsRebornPatchPlaceBreakPlugin() {
    this(Clock.systemUTC());
  }

  @TestOnly
  public JobsRebornPatchPlaceBreakPlugin(@NotNull Clock clock) {
    super();
    this.clock = clock;
  }

  @Override
  public void onEnable() {
    BukkitLoggerFactory.provideBukkitLogger(getLogger());

    JobsRebornPatchPlaceBreakInjector injector = new JobsRebornPatchPlaceBreakInjector(this, clock);
    ListenerRegister listenerRegister = injector.listenerRegister();
    MetricsFacade metricsFacade = injector.metricsFacade();
    patchPlaceBreakCore = injector.patchPlaceBreakCore();
    patchPlaceBreakPaperAdapterApi = injector.patchPlaceBreakPaperAdapterApi();

    listenerRegister.registerListeners();
    metricsFacade.activateMetricsCollection();

    getLogger().info("JobsReborn-PatchPlaceBreak successfully enabled.");
  }

  @Override
  public void onDisable() {
    if (patchPlaceBreakCore != null) {
      patchPlaceBreakCore.disable();
    }
    getLogger().info("JobsReborn-PatchPlaceBreak successfully disabled.");
  }

  @NotNull
  public PatchPlaceBreakPaperAdapterApi patchPlaceBreakPaperAdapterApi() {
    return patchPlaceBreakPaperAdapterApi;
  }
}
