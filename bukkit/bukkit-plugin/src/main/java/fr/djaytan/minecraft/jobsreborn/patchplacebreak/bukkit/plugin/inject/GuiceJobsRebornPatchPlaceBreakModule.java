package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject;

import java.nio.file.Path;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreak;
import lombok.NonNull;

public class GuiceJobsRebornPatchPlaceBreakModule extends AbstractModule {

  @Provides
  @Singleton
  public @NonNull PatchPlaceBreakApi providePatchPlaceBreakApi(ClassLoader pluginClassLoader,
      PatchPlaceBreak patchPlaceBreak, @Named("dataFolder") Path dataFolder) {
    return patchPlaceBreak.enable(pluginClassLoader, dataFolder);
  }
}
