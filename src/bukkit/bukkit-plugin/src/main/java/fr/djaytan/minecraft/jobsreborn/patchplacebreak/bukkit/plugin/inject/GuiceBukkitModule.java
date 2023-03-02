/*-
 * #%L
 * JobsReborn-PatchPlaceBreak
 * %%
 * Copyright (C) 2022 - 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 * %%
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
 * #L%
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

@RequiredArgsConstructor
public class GuiceBukkitModule extends AbstractModule {

  private final JavaPlugin javaPlugin;

  @Override
  protected void configure() {
    bind(JavaPlugin.class).toInstance(javaPlugin);
    bind(Server.class).toInstance(javaPlugin.getServer());
    bind(PluginManager.class).toInstance(javaPlugin.getServer().getPluginManager());
    bind(ServicesManager.class).toInstance(javaPlugin.getServer().getServicesManager());
    bind(ItemFactory.class).toInstance(javaPlugin.getServer().getItemFactory());
    bind(ConsoleCommandSender.class).toInstance(javaPlugin.getServer().getConsoleSender());
    bind(BukkitScheduler.class).toInstance(javaPlugin.getServer().getScheduler());
  }

  @Provides
  @Named("dataFolder")
  @Singleton
  public @NonNull Path provideDataFolder() {
    return javaPlugin.getDataFolder().toPath();
  }

  @Provides
  @Singleton
  public @NonNull Executor provideMainThreadExecutor(BukkitScheduler bukkitScheduler) {
    return runnable -> bukkitScheduler.runTask(javaPlugin, runnable);
  }
}
