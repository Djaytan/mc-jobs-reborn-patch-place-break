package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import lombok.ToString;
import lombok.Value;

@Value(staticConstructor = "of")
@ToString(exclude = {"password"})
public class DataSourceCredentialsProperties {

  String username;
  String password;
}
