package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.ValidatingConvertibleProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.CredentialsProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

/** Represents an annotated Java Beans version of a {@link CredentialsProperties}. */
@ConfigSerializable
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "password")
@NoArgsConstructor(staticName = "ofDefault")
@AllArgsConstructor(staticName = "of")
public final class CredentialsValidatingProperties
    extends ValidatingConvertibleProperties<CredentialsProperties> {

  @NotBlank
  @Size(max = 32)
  @Required
  @Comment(
      "Under behalf of which user to connect on the DBMS server\n"
          + "Value can't be empty or blank")
  private String username = "username";

  @NotNull
  @Size(max = 128)
  @Required
  @Comment("Password of the user (optional but highly recommended)")
  private String password = "password";

  @Override
  protected @NonNull CredentialsProperties convertValidated() {
    return CredentialsProperties.of(username, password);
  }
}
