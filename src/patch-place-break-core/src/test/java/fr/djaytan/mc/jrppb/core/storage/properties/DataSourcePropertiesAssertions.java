package fr.djaytan.mc.jrppb.core.storage.properties;

import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_POOL_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_DATA_SOURCE_TYPE;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jetbrains.annotations.NotNull;

public final class DataSourcePropertiesAssertions {

  public static void assertSuccessfulInstantiation(
      @NotNull DataSourceType type,
      @NotNull String tableName,
      @NotNull DbmsServerProperties dbmsServer,
      @NotNull ConnectionPoolProperties connectionPool) {
    assertThat(new DataSourceProperties(type, tableName, dbmsServer, connectionPool))
        .satisfies(v -> assertThat(v.type()).isEqualTo(type))
        .satisfies(v -> assertThat(v.tableName()).isEqualTo(tableName))
        .satisfies(v -> assertThat(v.dbmsServer()).isEqualTo(dbmsServer))
        .satisfies(v -> assertThat(v.connectionPool()).isEqualTo(connectionPool));
  }

  public static void assertInstantiationFailureWithBlankTableName(@NotNull String tableName) {
    assertThatThrownBy(
            () ->
                new DataSourceProperties(
                    NOMINAL_DATA_SOURCE_TYPE,
                    tableName,
                    NOMINAL_DBMS_SERVER_PROPERTIES,
                    NOMINAL_CONNECTION_POOL_PROPERTIES))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The data source table name must not be blank");
  }
}
