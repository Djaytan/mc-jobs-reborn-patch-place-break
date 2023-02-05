package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class CredentialsPropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    String username = "username1";
    String password = "passworrd2";

    // When
    CredentialsProperties credentialsProperties = CredentialsProperties.of(username, password);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertEquals(username, credentialsProperties.getUsername()),
        () -> assertEquals(password, credentialsProperties.getPassword()));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(CredentialsProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(CredentialsProperties.class).withClassName(NameStyle.SIMPLE_NAME)
        .withIgnoredFields("password").verify();
  }
}
