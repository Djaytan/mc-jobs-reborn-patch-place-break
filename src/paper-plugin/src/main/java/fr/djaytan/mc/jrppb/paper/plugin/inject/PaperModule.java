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
package fr.djaytan.mc.jrppb.paper.plugin.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

final class PaperModule extends AbstractModule {

  private final JavaPlugin javaPlugin;

  PaperModule(@NotNull JavaPlugin javaPlugin) {
    this.javaPlugin = javaPlugin;
  }

  @Provides
  @Singleton
  @NotNull
  JavaPlugin javaPlugin() {
    return javaPlugin;
  }

  @Provides
  @Singleton
  @NotNull
  Server server() {
    return javaPlugin.getServer();
  }

  @Provides
  @Singleton
  @NotNull
  PluginManager pluginManager(@NotNull Server server) {
    return server.getPluginManager();
  }

  @Provides
  @Named("dataFolder")
  @Singleton
  @NotNull
  Path dataFolder() throws IOException {
    Path dataFolder = javaPlugin.getDataFolder().toPath();
    Files.createDirectories(dataFolder);
    return dataFolder;
  }

  @Provides
  @Singleton
  @NotNull
  ClassLoader classLoader() {
    return javaPlugin.getClass().getClassLoader();
  }
}
