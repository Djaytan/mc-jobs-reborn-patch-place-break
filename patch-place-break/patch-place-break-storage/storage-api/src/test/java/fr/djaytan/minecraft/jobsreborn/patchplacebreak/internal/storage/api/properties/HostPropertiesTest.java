package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class HostPropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    String hostname = "host";
    int port = 80;
    boolean isSslEnabled = true;

    // When
    DbmsHostProperties hostProperties = DbmsHostProperties.of("host", 80, true);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertEquals(hostname, hostProperties.getHostname()),
        () -> assertEquals(port, hostProperties.getPort()),
        () -> assertEquals(isSslEnabled, hostProperties.isSslEnabled()));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(DbmsHostProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(DbmsHostProperties.class).withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
