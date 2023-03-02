package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DbmsServerPropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    DbmsHostProperties hostProperties = DbmsHostProperties.of("host", 80, true);
    CredentialsProperties credentialsProperties = CredentialsProperties.of("username", "password");
    String database = "test_db";

    // When
    DbmsServerProperties dbmsServerProperties =
        DbmsServerProperties.of(hostProperties, credentialsProperties, database);

    // Then
    assertAll(
        "Verification of returned values from getters",
        () -> assertThat(dbmsServerProperties.getHost()).isEqualTo(hostProperties),
        () -> assertThat(dbmsServerProperties.getCredentials()).isEqualTo(credentialsProperties),
        () -> assertThat(dbmsServerProperties.getDatabase()).isEqualTo(database));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(DbmsServerProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(DbmsServerProperties.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
