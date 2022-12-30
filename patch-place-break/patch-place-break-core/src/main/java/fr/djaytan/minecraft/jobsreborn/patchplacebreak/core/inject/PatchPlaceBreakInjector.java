package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject;

import java.nio.file.Path;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakCoreException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.DataSource;
import lombok.NonNull;

public final class PatchPlaceBreakInjector {

  private Injector injector;

  public void inject(@NonNull ClassLoader classLoader, @NonNull Path dataFolder) {
    Module patchPlaceBreakModule = new GuicePatchPlaceBreakModule(classLoader, dataFolder);
    injector = Guice.createInjector(patchPlaceBreakModule);
  }

  public @NonNull PatchPlaceBreakApi getPatchApi() {
    if (injector == null) {
      throw PatchPlaceBreakCoreException.injectionRequiredFirst();
    }
    return injector.getInstance(PatchPlaceBreakApi.class);
  }

  public @NonNull DataSource getDataSource() {
    if (injector == null) {
      throw PatchPlaceBreakCoreException.injectionRequiredFirst();
    }
    return injector.getInstance(DataSource.class);
  }
}
