package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import java.nio.file.Path;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

// TODO: create custom builder (some properties are mandatory according to the data source type)
@Value
@Builder
public class DataSourceProperties {

  @NonNull
  DataSourceType type;
  DataSourceHostProperties host;
  DataSourceCredentialsProperties credentials;
  @Builder.Default
  String databaseName = "patch_place_break";
  @Builder.Default
  String tableName = "patch_place_break_tag";
  @NonNull
  Path dataFolder;
}
