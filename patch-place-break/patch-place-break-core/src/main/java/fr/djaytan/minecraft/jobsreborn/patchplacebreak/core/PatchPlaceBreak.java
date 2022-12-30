package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import java.nio.file.Path;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.PatchPlaceBreakInjector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.DataSource;
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
public class PatchPlaceBreak {

  private DataSource dataSource;

  public @NonNull PatchPlaceBreakApi enable(@NonNull ClassLoader classLoader,
      @NonNull Path dataFolder) throws PatchPlaceBreakException {
    PatchPlaceBreakInjector injector = new PatchPlaceBreakInjector();
    injector.inject(classLoader, dataFolder);
    dataSource = injector.getDataSource();
    dataSource.connect();
    return injector.getPatchApi();
  }

  public void disable() throws PatchPlaceBreakException {
    dataSource.disconnect();
  }
}
