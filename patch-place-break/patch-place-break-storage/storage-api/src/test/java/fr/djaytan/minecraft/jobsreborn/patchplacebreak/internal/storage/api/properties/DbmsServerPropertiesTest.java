package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class DbmsServerPropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    HostProperties hostProperties = HostProperties.of("host", 80, true);
    CredentialsProperties credentialsProperties = CredentialsProperties.of("username", "password");
    String database = "testdb";

    // When
    DbmsServerProperties dbmsServerProperties =
        DbmsServerProperties.of(hostProperties, credentialsProperties, database);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertEquals(hostProperties, dbmsServerProperties.getHost()),
        () -> assertEquals(credentialsProperties, dbmsServerProperties.getCredentials()),
        () -> assertEquals(database, dbmsServerProperties.getDatabase()));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(DbmsServerProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(DbmsServerProperties.class).withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
