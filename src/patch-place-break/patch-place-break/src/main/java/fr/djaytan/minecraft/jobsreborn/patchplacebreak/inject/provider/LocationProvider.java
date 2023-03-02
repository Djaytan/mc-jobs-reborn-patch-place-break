package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import javax.inject.Inject;
import javax.inject.Provider;
import lombok.NonNull;
import org.flywaydb.core.api.Location;

public class LocationProvider implements Provider<Location> {

  private static final String DB_MIGRATION_DESCRIPTOR_FORMAT = "/db/migration/%s";

  private final DataSourceProperties dataSourceProperties;

  @Inject
  public LocationProvider(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  @Override
  public @NonNull Location get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case MYSQL:
      case SQLITE:
        {
          String descriptor =
              String.format(DB_MIGRATION_DESCRIPTOR_FORMAT, dataSourceType.name().toLowerCase());
          return new Location(descriptor);
        }
      default:
        {
          throw PatchPlaceBreakException.unsupportedDataSourceType(dataSourceType);
        }
    }
  }
}
