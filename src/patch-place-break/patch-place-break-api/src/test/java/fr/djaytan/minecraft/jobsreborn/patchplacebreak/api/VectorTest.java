package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VectorTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    int modX = 152;
    int modY = -2;
    int modZ = 999;

    // When
    Vector vector = Vector.of(modX, modY, modZ);

    // Then
    assertAll(
        "Verification of returned values from getters",
        () -> assertThat(vector.getModX()).isEqualTo(modX),
        () -> assertThat(vector.getModY()).isEqualTo(modY),
        () -> assertThat(vector.getModZ()).isEqualTo(modZ));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(Vector.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(Vector.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
