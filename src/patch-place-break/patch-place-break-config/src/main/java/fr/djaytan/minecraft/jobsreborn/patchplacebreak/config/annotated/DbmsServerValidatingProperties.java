package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.ValidatingConvertibleProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsServerProperties;
import jakarta.validation.Valid;
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

/** Represents an annotated Java Beans version of a {@link DbmsServerProperties}. */
@ConfigSerializable
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(staticName = "ofDefault")
@AllArgsConstructor(staticName = "of")
public final class DbmsServerValidatingProperties
    extends ValidatingConvertibleProperties<DbmsServerProperties> {

  @NotNull
  @Valid
  @Required
  @Comment("Host properties of the DBMS server")
  private DbmsHostValidatingProperties host = DbmsHostValidatingProperties.ofDefault();

  @NotNull
  @Valid
  @Required
  @Comment("Credentials for authentication with the DBMS server")
  private CredentialsValidatingProperties credentials = CredentialsValidatingProperties.ofDefault();

  @NotBlank
  @Size(max = 128)
  @Required
  @Comment("The database to use on DBMS server\n" + "Value can't be empty or blank")
  private String database = "database";

  @Override
  protected @NonNull DbmsServerProperties convertValidated() {
    DbmsHostProperties dbmsHostProperties = host.convertValidated();
    CredentialsProperties credentialsProperties = credentials.convertValidated();
    return DbmsServerProperties.of(dbmsHostProperties, credentialsProperties, database);
  }
}
