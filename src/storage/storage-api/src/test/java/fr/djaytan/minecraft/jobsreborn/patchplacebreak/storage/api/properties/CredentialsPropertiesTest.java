package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CredentialsPropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    String username = "username1";
    String password = "password2";

    // When
    CredentialsProperties credentialsProperties = CredentialsProperties.of(username, password);

    // Then
    assertAll(
        "Verification of returned values from getters",
        () -> assertThat(credentialsProperties.getUsername()).isEqualTo(username),
        () -> assertThat(credentialsProperties.getPassword()).isEqualTo(password));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(CredentialsProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(CredentialsProperties.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withIgnoredFields("password")
        .verify();
  }
}
