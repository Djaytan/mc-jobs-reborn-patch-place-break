package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import lombok.Value;

@Value(staticConstructor = "of")
public class DataSourceHostProperties {

  String hostName;
  int port;
  boolean isSslEnabled;
}
