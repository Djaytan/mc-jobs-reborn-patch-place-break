package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class HostProperties {

  @NonNull
  String hostname;
  int port;
  boolean isSslEnabled;
}
