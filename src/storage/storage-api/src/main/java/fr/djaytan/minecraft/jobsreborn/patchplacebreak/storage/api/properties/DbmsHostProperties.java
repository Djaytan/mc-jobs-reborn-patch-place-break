package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class DbmsHostProperties {

  @NonNull String hostname;
  int port;
  boolean isSslEnabled;
}
