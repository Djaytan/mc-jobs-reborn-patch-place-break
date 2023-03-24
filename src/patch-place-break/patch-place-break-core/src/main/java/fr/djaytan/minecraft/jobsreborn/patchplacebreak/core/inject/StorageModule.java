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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlDataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.access.SqlTagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.provider.FlywayProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.provider.HikariDataSourceProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.provider.JdbcUrlProvider;
import java.nio.file.Path;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

final class StorageModule extends AbstractModule {

  private static final String SQLITE_DATABASE_FILE_NAME = "sqlite-data.db";

  @Override
  protected void configure() {
    bind(DataSourceManager.class).to(SqlDataSourceManager.class).in(Singleton.class);
    bind(TagRepository.class).to(SqlTagRepository.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  static @NotNull Flyway flyway(@NotNull FlywayProvider flywayProvider) {
    return flywayProvider.get();
  }

  @Provides
  @Singleton
  static @NotNull HikariDataSource hikariDataSource(
      @NotNull HikariDataSourceProvider hikariDataSourceProvider) {
    return hikariDataSourceProvider.get();
  }

  @Provides
  @Singleton
  static @NotNull JdbcUrl jdbcUrl(@NotNull JdbcUrlProvider jdbcUrlProvider) {
    return jdbcUrlProvider.get();
  }

  @Provides
  @Singleton
  static @NotNull DataSource dataSource(@NotNull HikariDataSource hikariDataSource) {
    return hikariDataSource;
  }

  @Provides
  @Named("sqliteDatabaseFile")
  @Singleton
  static @NotNull Path sqliteDatabaseFile(@NotNull @Named("dataFolder") Path dataFolder) {
    return dataFolder.resolve(SQLITE_DATABASE_FILE_NAME);
  }
}
