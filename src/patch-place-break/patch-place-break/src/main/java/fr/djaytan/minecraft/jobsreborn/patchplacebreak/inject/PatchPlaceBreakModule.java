package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.PatchPlaceBreakImpl;
import java.nio.file.Path;
import lombok.NonNull;

/** Represents general patch configs. */
public class PatchPlaceBreakModule extends AbstractModule {

  private final ClassLoader classLoader;
  private final Path dataFolder;

  public PatchPlaceBreakModule(@NonNull ClassLoader classLoader, @NonNull Path dataFolder) {
    this.classLoader = classLoader;
    this.dataFolder = dataFolder;
  }

  @Override
  protected void configure() {
    bind(PatchPlaceBreakApi.class).to(PatchPlaceBreakImpl.class);
  }

  @Provides
  @Singleton
  public @NonNull ClassLoader provideClassLoader() {
    return classLoader;
  }

  @Provides
  @Named("dataFolder")
  @Singleton
  public @NonNull Path provideDataFolder() {
    // TODO: create folder if doesn't exists (same to do in bukkit-plugin side if applicable)
    return dataFolder;
  }
}
