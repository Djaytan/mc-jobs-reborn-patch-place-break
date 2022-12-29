package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.DataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import lombok.NonNull;

public final class PatchPlaceBreakInjector {

  private Injector injector;

  public void inject(@NonNull DataSourceProperties dataSourceProperties) {
    Module patchPlaceBreakModule = new GuicePatchPlaceBreakModule(dataSourceProperties);
    injector = Guice.createInjector(patchPlaceBreakModule);
  }

  public @NonNull PatchPlaceBreakApi getPatchApi() {
    return injector.getInstance(PatchPlaceBreakApi.class);
  }

  public @NonNull DataSource getDataSource() {
    return injector.getInstance(DataSource.class);
  }
}
