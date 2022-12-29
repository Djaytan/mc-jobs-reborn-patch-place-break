package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject;

import com.google.inject.AbstractModule;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.ConnectionPoolProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.DataSourceProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.SqlDataSourceInitializerProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.TagRepositoryProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.TagSqlDataDefinerProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.PatchPlaceBreakDefault;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.DataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.ConnectionPool;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.TagSqlDataDefiner;
import lombok.NonNull;

public class GuicePatchPlaceBreakModule extends AbstractModule {

  private final DataSourceProperties dataSourceProperties;

  public GuicePatchPlaceBreakModule(@NonNull DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  @Override
  protected void configure() {
    bind(DataSourceProperties.class).toInstance(dataSourceProperties);

    bind(PatchPlaceBreakApi.class).to(PatchPlaceBreakDefault.class);

    bind(ConnectionPool.class).toProvider(ConnectionPoolProvider.class);
    bind(DataSource.class).toProvider(DataSourceProvider.class);
    bind(SqlDataSourceInitializer.class).toProvider(SqlDataSourceInitializerProvider.class);
    bind(TagRepository.class).toProvider(TagRepositoryProvider.class);
    bind(TagSqlDataDefiner.class).toProvider(TagSqlDataDefinerProvider.class);
  }
}
