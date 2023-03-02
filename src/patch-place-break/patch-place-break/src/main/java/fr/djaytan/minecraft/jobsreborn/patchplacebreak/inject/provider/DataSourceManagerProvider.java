package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.inmemory.InMemoryDataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlDataSourceManager;
import javax.inject.Inject;
import javax.inject.Provider;
import lombok.NonNull;

public class DataSourceManagerProvider implements Provider<DataSourceManager> {

  private final DataSourceProperties dataSourceProperties;
  private final InMemoryDataSourceManager inMemoryDataSource;
  private final SqlDataSourceManager sqlDataSource;

  @Inject
  public DataSourceManagerProvider(
      DataSourceProperties dataSourceProperties,
      InMemoryDataSourceManager inMemoryDataSource,
      SqlDataSourceManager sqlDataSource) {
    this.dataSourceProperties = dataSourceProperties;
    this.inMemoryDataSource = inMemoryDataSource;
    this.sqlDataSource = sqlDataSource;
  }

  @Override
  public @NonNull DataSourceManager get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case IN_MEMORY:
        {
          return inMemoryDataSource;
        }
      case MYSQL:
      case SQLITE:
        {
          return sqlDataSource;
        }
      default:
        {
          throw PatchPlaceBreakException.unrecognisedDataSourceType(dataSourceType);
        }
    }
  }
}
