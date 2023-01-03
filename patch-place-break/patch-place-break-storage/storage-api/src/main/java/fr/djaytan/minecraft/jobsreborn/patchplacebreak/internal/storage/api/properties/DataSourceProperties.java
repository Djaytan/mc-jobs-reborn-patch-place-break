package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class DataSourceProperties {

  @NonNull
  DataSourceType type;
  @NonNull
  String table;
  @NonNull
  DbmsServerProperties dbmsServer;
}
