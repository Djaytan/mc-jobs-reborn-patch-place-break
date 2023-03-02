package fr.djaytan.minecraft.jobsreborn.patchplacebreak;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.PatchPlaceBreakInjector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import java.nio.file.Path;
import javax.inject.Singleton;
import lombok.NonNull;

/**
 * This class represents the patch place-break enabler for any consumer. Its role is to manage the
 * patch's lifecycle.
 *
 * <p>When enabling the patch with {@link #enable(ClassLoader, Path)}, it is ensured to be properly
 * initialized and a {@link PatchPlaceBreakApi} instance is returned to be consumed by the caller.
 *
 * <p>When no longer using the patch, ensure to call the {@link #disable()} method (e.g. on plugin
 * disabling). This will ensure to properly disable the patch (e.g. by closing existing databases
 * connections).
 *
 * <p>Only one instance of this class is expected to be run at once.
 */
@Singleton
public class PatchPlaceBreak {

  private DataSourceManager dataSourceManager;

  public @NonNull PatchPlaceBreakApi enable(
      @NonNull ClassLoader classLoader, @NonNull Path dataFolder) {
    setupSlf4j();
    PatchPlaceBreakInjector injector = new PatchPlaceBreakInjector();
    injector.inject(classLoader, dataFolder);
    dataSourceManager = injector.getDataSourceManager();
    dataSourceManager.connect();
    return injector.getPatchApi();
  }

  public void disable() {
    dataSourceManager.disconnect();
  }

  /**
   * jboss-logging shall use SLF4J as logger provider.
   *
   * <p>This is required since jboss-logging is a dependency of hibernate-validator.
   */
  private void setupSlf4j() {
    System.setProperty("org.jboss.logging.provider", "slf4j");
  }
}
