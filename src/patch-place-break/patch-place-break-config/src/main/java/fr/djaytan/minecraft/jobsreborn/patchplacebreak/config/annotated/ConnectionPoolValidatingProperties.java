package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.ValidatingConvertibleProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.ConnectionPoolProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

/** Represents an annotated Java Beans version of a {@link ConnectionPoolProperties}. */
@ConfigSerializable
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(staticName = "ofDefault")
@AllArgsConstructor(staticName = "of")
public final class ConnectionPoolValidatingProperties
    extends ValidatingConvertibleProperties<ConnectionPoolProperties> {

  @Max(600000)
  @Positive
  @Required
  @Comment(
      "The connection timeout in milliseconds\n"
          + "Corresponds to the maximum time the connection pool will wait to acquire a new connection\n"
          + "from the DBMS server\n"
          + "Not applicable for SQLite\n"
          + "Accepted range values: [1-600000]")
  private long connectionTimeout = 30000;

  @Max(100)
  @Positive
  @Required
  @Comment(
      "The number of DBMS connections in the pool\n"
          + "Could be best determined by the executing environment\n"
          + "Accepted range values: [1-100]")
  private int poolSize = 10;

  @Override
  protected @NonNull ConnectionPoolProperties convertValidated() {
    return ConnectionPoolProperties.of(connectionTimeout, poolSize);
  }
}
