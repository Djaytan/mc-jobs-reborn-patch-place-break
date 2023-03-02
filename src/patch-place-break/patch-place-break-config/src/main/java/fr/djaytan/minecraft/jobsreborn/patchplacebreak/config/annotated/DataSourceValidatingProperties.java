package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.ValidatingConvertibleProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
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

/** Represents an annotated Java Beans version of a {@link DataSourceProperties}. */
@ConfigSerializable
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(staticName = "ofDefault")
@AllArgsConstructor(staticName = "of")
public final class DataSourceValidatingProperties
    extends ValidatingConvertibleProperties<DataSourceProperties> {

  @NotNull
  @Required
  @Comment(
      "The type of datasource to use\n"
          + "Available types:\n"
          + "- SQLITE: use a local file as database (easy & fast setup)\n"
          + "- MYSQL: use a MySQL database server (better performances)")
  private DataSourceType type = DataSourceType.SQLITE;

  @NotBlank
  @Size(max = 128)
  @Required
  @Comment("The table where data will be stored\n" + "Value can't be empty or blank")
  private String table = "patch_place_break_tag";

  @NotNull
  @Valid
  @Required
  @Comment(
      "The DBMS server properties for connection establishment\n" + "Not applicable for SQLite")
  private DbmsServerValidatingProperties dbmsServer = DbmsServerValidatingProperties.ofDefault();

  @NotNull
  @Valid
  @Required
  @Comment(
      "Connection pool properties\n"
          + "This is reserved for advanced usage only\n"
          + "Change these settings only if you know what you are doing")
  private ConnectionPoolValidatingProperties connectionPool =
      ConnectionPoolValidatingProperties.ofDefault();

  @Override
  protected @NonNull DataSourceProperties convertValidated() {
    DbmsServerProperties dbmsServerProperties = dbmsServer.convertValidated();
    ConnectionPoolProperties connectionPoolProperties = connectionPool.convertValidated();
    return DataSourceProperties.of(type, table, dbmsServerProperties, connectionPoolProperties);
  }
}
