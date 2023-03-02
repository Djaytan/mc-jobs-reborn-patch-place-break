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
