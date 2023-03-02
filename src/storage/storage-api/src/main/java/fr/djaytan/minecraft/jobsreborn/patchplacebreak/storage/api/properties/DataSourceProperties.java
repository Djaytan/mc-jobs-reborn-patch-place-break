package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class DataSourceProperties {

  @NonNull DataSourceType type;
  @NonNull String table;
  @NonNull DbmsServerProperties dbmsServer;
  @NonNull ConnectionPoolProperties connectionPool;
}
