package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagVector;
import nl.jqno.equalsverifier.EqualsVerifier;

class TagVectorTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    double modX = 152.11D;
    double modY = -2D;
    double modZ = 999.6523D;

    // When
    TagVector tagVector = TagVector.of(modX, modY, modZ);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertEquals(modX, tagVector.getModX()),
        () -> assertEquals(modY, tagVector.getModY()),
        () -> assertEquals(modZ, tagVector.getModZ()));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(TagVector.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(TagVector.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
