package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public final class GuiceBukkitInjector {

  private GuiceBukkitInjector() {
    // Static class
  }

  public static void inject(@NonNull ClassLoader classLoader, @NonNull JavaPlugin javaPlugin) {
    GuiceBukkitModule bukkitModule = new GuiceBukkitModule(javaPlugin);
    GuiceJobsRebornPatchPlaceBreakModule jobsRebornPatchPlaceBreakModule =
        new GuiceJobsRebornPatchPlaceBreakModule(classLoader);
    Injector injector = Guice.createInjector(bukkitModule, jobsRebornPatchPlaceBreakModule);
    injector.injectMembers(javaPlugin);
    log.atInfo().log("Dependencies injected.");
  }
}
