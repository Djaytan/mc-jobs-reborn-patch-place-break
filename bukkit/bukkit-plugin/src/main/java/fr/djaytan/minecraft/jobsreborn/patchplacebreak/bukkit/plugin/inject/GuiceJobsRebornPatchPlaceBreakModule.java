package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject;

import java.nio.file.Path;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreak;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import lombok.NonNull;

public class GuiceJobsRebornPatchPlaceBreakModule extends AbstractModule {

  @Provides
  @Singleton
  public @NonNull DataSourceProperties provideDataSourceProperties(
      @Named("dataFolder") Path dataFolder) {
    return DataSourceProperties.builder().type(DataSourceType.SQLITE).dataFolder(dataFolder)
        .build();
  }

  @Provides
  @Singleton
  public @NonNull PatchPlaceBreak providePatchPlaceBreak() {
    return new PatchPlaceBreak();
  }

  @Provides
  @Singleton
  public @NonNull PatchPlaceBreakApi providePatchPlaceBreakApi(
      DataSourceProperties dataSourceProperties, PatchPlaceBreak patchPlaceBreak) {
    return patchPlaceBreak.enable(dataSourceProperties);
  }
}
