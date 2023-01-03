package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import lombok.Value;

@Value(staticConstructor = "of")
public class HostProperties {

  String hostname;
  int port;
  boolean isSslEnabled;
}
