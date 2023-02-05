package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class DataSourcePropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    DbmsServerProperties dbmsServerProperties =
        DbmsServerProperties.of(DbmsHostProperties.of("host", 80, true),
            CredentialsProperties.of("username", "passwoord"), "testdb");
    ConnectionPoolProperties connectionPoolProperties = ConnectionPoolProperties.of(120, 2);
    DataSourceType dataSourceType = DataSourceType.MYSQL;
    String table = "test";


    // When
    DataSourceProperties dataSourceProperties = DataSourceProperties.of(dataSourceType, table,
        dbmsServerProperties, connectionPoolProperties);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertEquals(dataSourceType, dataSourceProperties.getType()),
        () -> assertEquals(table, dataSourceProperties.getTable()),
        () -> assertEquals(dbmsServerProperties, dataSourceProperties.getDbmsServer()),
        () -> assertEquals(connectionPoolProperties, dataSourceProperties.getConnectionPool()));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(DataSourceProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(DataSourceProperties.class).withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
