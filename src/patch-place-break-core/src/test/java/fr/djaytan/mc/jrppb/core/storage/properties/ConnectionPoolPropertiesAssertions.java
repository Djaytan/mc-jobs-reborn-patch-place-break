package fr.djaytan.mc.jrppb.core.storage.properties;

import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_TIMEOUT;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_POOL_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class ConnectionPoolPropertiesAssertions {

  public static void assertSuccessfulInstantiation(int connectionPool, int poolSize) {
    assertThat(new ConnectionPoolProperties(connectionPool, poolSize))
        .satisfies(v -> assertThat(v.connectionTimeout()).isEqualTo(connectionPool))
        .satisfies(v -> assertThat(v.poolSize()).isEqualTo(poolSize));
  }

  public static void assertSuccessfulInstantiationWithConnectionPool(int connectionPool) {
    assertSuccessfulInstantiation(connectionPool, NOMINAL_POOL_SIZE);
  }

  public static void assertSuccessfulInstantiationWithPoolSize(int poolSize) {
    assertSuccessfulInstantiation(NOMINAL_CONNECTION_TIMEOUT, poolSize);
  }

  public static void assertInstantiationFailureWithInvalidConnectionTimeout(int connectionTimeout) {
    assertThatThrownBy(() -> new ConnectionPoolProperties(connectionTimeout, NOMINAL_POOL_SIZE))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The connection timeout must be between 1 and 600000");
  }

  public static void assertInstantiationFailureWithInvalidPoolSize(int invalidPoolSize) {
    assertThatThrownBy(
            () -> new ConnectionPoolProperties(NOMINAL_CONNECTION_TIMEOUT, invalidPoolSize))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The pool size must be between 1 and 100");
  }
}
