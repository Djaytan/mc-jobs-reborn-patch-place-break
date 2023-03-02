package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value(staticConstructor = "of")
@ToString(exclude = {"password"})
public class CredentialsProperties {

  @NonNull String username;
  @NonNull String password;
}
