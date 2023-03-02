package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class DbmsServerProperties {

  @NonNull DbmsHostProperties host;
  @NonNull CredentialsProperties credentials;
  @NonNull String database;
}
