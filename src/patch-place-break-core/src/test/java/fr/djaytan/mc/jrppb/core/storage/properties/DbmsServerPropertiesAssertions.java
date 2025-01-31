package fr.djaytan.mc.jrppb.core.storage.properties;

import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerCredentialsPropertiesTestDataSet.NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOST_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jetbrains.annotations.NotNull;

public final class DbmsServerPropertiesAssertions {

  public static void assertSuccessfulInstantiation(
      @NotNull DbmsServerHostProperties host,
      @NotNull DbmsServerCredentialsProperties credentials,
      @NotNull String databaseName) {
    assertThat(new DbmsServerProperties(host, credentials, databaseName))
        .satisfies(v -> assertThat(v.host()).isEqualTo(host))
        .satisfies(v -> assertThat(v.credentials()).isEqualTo(credentials))
        .satisfies(v -> assertThat(v.databaseName()).isEqualTo(databaseName));
  }

  public static void assertInstantiationFailureWithBlankDatabaseName(@NotNull String databaseName) {
    assertThatThrownBy(
            () ->
                new DbmsServerProperties(
                    NOMINAL_DBMS_SERVER_HOST_PROPERTIES,
                    NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES,
                    databaseName))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The DBMS server database name cannot be blank");
  }
}
