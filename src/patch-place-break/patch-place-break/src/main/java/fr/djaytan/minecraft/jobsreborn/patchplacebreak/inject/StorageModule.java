package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider.DataSourceInitializerProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider.DataSourceManagerProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider.JdbcUrlProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider.LocationProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider.TagRepositoryProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import java.nio.file.Path;
import javax.inject.Named;
import lombok.NonNull;
import org.flywaydb.core.api.Location;

public class StorageModule extends AbstractModule {

  private static final String SQLITE_DATABASE_FILE_NAME = "sqlite-data.db";

  @Override
  protected void configure() {
    bind(DataSourceManager.class).toProvider(DataSourceManagerProvider.class);
    bind(DataSourceInitializer.class).toProvider(DataSourceInitializerProvider.class);
    bind(JdbcUrl.class).toProvider(JdbcUrlProvider.class);
    bind(Location.class).toProvider(LocationProvider.class);
    bind(TagRepository.class).toProvider(TagRepositoryProvider.class);
  }

  @Provides
  @Named("sqliteDatabaseFile")
  @Singleton
  public @NonNull Path provideSqliteDatabasePath(@Named("dataFolder") Path dataFolder) {
    return dataFolder.resolve(SQLITE_DATABASE_FILE_NAME);
  }
}
