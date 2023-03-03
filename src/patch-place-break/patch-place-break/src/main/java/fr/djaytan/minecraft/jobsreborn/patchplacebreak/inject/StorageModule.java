/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
