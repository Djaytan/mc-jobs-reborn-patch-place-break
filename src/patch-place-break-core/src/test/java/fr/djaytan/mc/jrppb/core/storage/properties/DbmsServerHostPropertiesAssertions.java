package fr.djaytan.mc.jrppb.core.storage.properties;

import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOSTNAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_IS_SSL_ENABLED;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jetbrains.annotations.NotNull;

public final class DbmsServerHostPropertiesAssertions {

  public static void assertSuccessfulInstantiation(
      @NotNull String hostname, int port, boolean isSslEnabled) {
    assertThat(new DbmsServerHostProperties(hostname, port, isSslEnabled))
        .satisfies(v -> assertThat(v.hostname()).isEqualTo(hostname))
        .satisfies(v -> assertThat(v.port()).isEqualTo(port))
        .satisfies(v -> assertThat(v.isSslEnabled()).isEqualTo(isSslEnabled));
  }

  public static void assertSuccessfulInstantiationWithHostname(@NotNull String hostname) {
    assertThatCode(
            () ->
                new DbmsServerHostProperties(
                    hostname, NOMINAL_DBMS_SERVER_PORT, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .doesNotThrowAnyException();
  }

  public static void assertSuccessfulInstantiationWithPort(int port) {
    assertThatCode(
            () ->
                new DbmsServerHostProperties(
                    NOMINAL_DBMS_SERVER_HOSTNAME, port, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .doesNotThrowAnyException();
  }

  public static void assertInstantiationFailureWithTooLongHostname(@NotNull String hostname) {
    assertThatThrownBy(
            () ->
                new DbmsServerHostProperties(
                    hostname, NOMINAL_DBMS_SERVER_PORT, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The DBMS server hostname cannot exceed 255 characters");
  }

  public static void assertInstantiationFailureWithBlankHostname(@NotNull String hostname) {
    assertThatThrownBy(
            () ->
                new DbmsServerHostProperties(
                    hostname, NOMINAL_DBMS_SERVER_PORT, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The DBMS server hostname cannot be blank");
  }

  public static void assertInstantiationFailureWithInvalidPort(int port) {
    assertThatThrownBy(
            () ->
                new DbmsServerHostProperties(
                    NOMINAL_DBMS_SERVER_HOSTNAME, port, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The DBMS server port must be between 1 and 65535");
  }
}
