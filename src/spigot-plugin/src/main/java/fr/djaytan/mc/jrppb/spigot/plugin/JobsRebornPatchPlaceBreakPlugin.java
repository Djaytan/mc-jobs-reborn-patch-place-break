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
package fr.djaytan.mc.jrppb.spigot.plugin;

import com.djaytan.bukkit.slf4j.BukkitLoggerFactory;
import fr.djaytan.mc.jrppb.core.PatchPlaceBreakCore;
import fr.djaytan.mc.jrppb.spigot.adapter.PatchPlaceBreakSpigotAdapterApi;
import fr.djaytan.mc.jrppb.spigot.listener.ListenerRegister;
import fr.djaytan.mc.jrppb.spigot.plugin.inject.JobsRebornPatchPlaceBreakInjector;
import java.io.File;
import java.time.Clock;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

/** This class represents a JobsReborn patch place-break plugin. */
@SuppressWarnings("unused") // Instantiated by Bukkit's implementation (i.e. CraftBukkit)
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  private final Clock clock;
  private PatchPlaceBreakSpigotAdapterApi patchPlaceBreakSpigotAdapterApi;
  private PatchPlaceBreakCore patchPlaceBreakCore;

  public JobsRebornPatchPlaceBreakPlugin() {
    super();
    this.clock = Clock.systemUTC();
  }

  /** Required by MockBukkit. */
  public JobsRebornPatchPlaceBreakPlugin(
      JavaPluginLoader loader,
      PluginDescriptionFile description,
      File dataFolder,
      File file,
      @NotNull Clock clock) {
    super(loader, description, dataFolder, file);
    this.clock = clock;
  }

  @Override
  public void onEnable() {
    BukkitLoggerFactory.provideBukkitLogger(getLogger());

    JobsRebornPatchPlaceBreakInjector injector = new JobsRebornPatchPlaceBreakInjector(this, clock);
    ListenerRegister listenerRegister = injector.listenerRegister();
    MetricsFacade metricsFacade = injector.metricsFacade();
    patchPlaceBreakCore = injector.patchPlaceBreakCore();
    patchPlaceBreakSpigotAdapterApi = injector.patchPlaceBreakSpigotAdapterApi();

    listenerRegister.registerListeners();
    metricsFacade.activateMetricsCollection();

    getLogger().info("JobsReborn-PatchPlaceBreak successfully enabled.");
  }

  @Override
  public void onDisable() {
    patchPlaceBreakCore.disable();
    getLogger().info("JobsReborn-PatchPlaceBreak successfully disabled.");
  }

  @NotNull
  public PatchPlaceBreakSpigotAdapterApi patchPlaceBreakSpigotAdapterApi() {
    return patchPlaceBreakSpigotAdapterApi;
  }
}
