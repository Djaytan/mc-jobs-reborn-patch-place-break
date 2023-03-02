package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreak;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import java.nio.file.Path;
import lombok.NonNull;

public class GuiceJobsRebornPatchPlaceBreakModule extends AbstractModule {

  private final ClassLoader classLoader;

  public GuiceJobsRebornPatchPlaceBreakModule(@NonNull ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Provides
  @Singleton
  public @NonNull PatchPlaceBreakApi providePatchPlaceBreakApi(
      @Named("dataFolder") Path dataFolder, PatchPlaceBreak patchPlaceBreak) {
    return patchPlaceBreak.enable(classLoader, dataFolder);
  }
}
