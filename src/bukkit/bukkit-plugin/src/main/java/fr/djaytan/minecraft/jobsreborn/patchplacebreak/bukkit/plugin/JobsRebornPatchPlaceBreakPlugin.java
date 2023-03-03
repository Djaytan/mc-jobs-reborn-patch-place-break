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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreak;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.ListenerRegister;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject.GuiceBukkitInjector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.slf4j.BukkitLoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

/** This class represents a JobsReborn patch place-break plugin. */
@SuppressWarnings("unused") // Instantiated by Bukkit's implementation (i.e. CraftBukkit)
public class JobsRebornPatchPlaceBreakPlugin extends JavaPlugin {

  @Inject private ListenerRegister listenerRegister;
  @Inject private MetricsFacade metricsFacade;
  @Inject private PatchPlaceBreak patchPlaceBreak;

  @Override
  @SneakyThrows
  public void onEnable() {
    createFolderIfNotExist(getDataFolder().toPath());
    enableSlf4j(getLogger());
    GuiceBukkitInjector.inject(getClassLoader(), this);
    listenerRegister.registerListeners();
    metricsFacade.activateMetricsCollection();
    getLogger().info("JobsReborn-PatchPlaceBreak successfully enabled.");
  }

  @Override
  public void onDisable() {
    patchPlaceBreak.disable();
    getLogger().info("JobsReborn-PatchPlaceBreak successfully disabled.");
  }

  private static void createFolderIfNotExist(@NonNull Path dataFolder) throws IOException {
    Files.createDirectories(dataFolder);
  }

  private static void enableSlf4j(@NonNull Logger bukkitLogger) {
    BukkitLoggerFactory.provideBukkitLogger(bukkitLogger);
  }
}
