package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import lombok.Value;

@Value(staticConstructor = "of")
public class ConnectionPoolProperties {

  long connectionTimeout;
  int poolSize;
}
