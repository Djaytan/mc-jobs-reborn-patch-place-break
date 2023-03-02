package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import java.nio.file.Path;
import lombok.NonNull;

public final class PatchPlaceBreakInjector {

  private Injector injector;

  public void inject(@NonNull ClassLoader classLoader, @NonNull Path dataFolder) {
    injector =
        Guice.createInjector(
            new ConfigModule(),
            new StorageModule(),
            new ValidationModule(),
            new PatchPlaceBreakModule(classLoader, dataFolder));
  }

  public @NonNull PatchPlaceBreakApi getPatchApi() {
    if (injector == null) {
      throw PatchPlaceBreakException.injectionRequiredFirst();
    }
    return injector.getInstance(PatchPlaceBreakApi.class);
  }

  public @NonNull DataSourceManager getDataSourceManager() {
    if (injector == null) {
      throw PatchPlaceBreakException.injectionRequiredFirst();
    }
    return injector.getInstance(DataSourceManager.class);
  }
}
