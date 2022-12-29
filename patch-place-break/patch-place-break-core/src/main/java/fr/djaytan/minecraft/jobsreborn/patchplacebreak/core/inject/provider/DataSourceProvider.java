package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakStandaloneException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.DataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.inmemory.InMemoryDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.SqlDataSource;

public class DataSourceProvider implements Provider<DataSource> {

  private final DataSourceProperties dataSourceProperties;
  private final InMemoryDataSource inMemoryDataSource;
  private final SqlDataSource sqlDataSource;

  @Inject
  DataSourceProvider(DataSourceProperties dataSourceProperties,
      InMemoryDataSource inMemoryDataSource, SqlDataSource sqlDataSource) {
    this.dataSourceProperties = dataSourceProperties;
    this.inMemoryDataSource = inMemoryDataSource;
    this.sqlDataSource = sqlDataSource;
  }

  @Override
  public DataSource get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case IN_MEMORY: {
        return inMemoryDataSource;
      }
      case MYSQL:
      case SQLITE: {
        return sqlDataSource;
      }
      default: {
        throw PatchPlaceBreakStandaloneException.unrecognisedDataSourceType(dataSourceType);
      }
    }
  }
}
