package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConnectionPoolPropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    long connectionTimeout = 158652381;
    int poolSize = 12;

    // When
    ConnectionPoolProperties connectionPoolProperties =
        ConnectionPoolProperties.of(connectionTimeout, poolSize);

    // Then
    assertAll(
        "Verification of returned values from getters",
        () ->
            assertThat(connectionPoolProperties.getConnectionTimeout())
                .isEqualTo(connectionTimeout),
        () -> assertThat(connectionPoolProperties.getPoolSize()).isEqualTo(poolSize));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(ConnectionPoolProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(ConnectionPoolProperties.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
