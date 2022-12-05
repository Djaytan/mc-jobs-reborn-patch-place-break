package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class TagTest {

  /**
   * Given required data to create a PatchPlaceAndBreakTag,
   * When calling the constructor with these data,
   * Then the PatchPlaceAndBreakTag is created successfully.
   */
  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    UUID uuid = UUID.randomUUID();
    LocalDateTime localDateTime = LocalDateTime.now();
    boolean isEphemeral = true;

    String worldName = "world";
    double x = 52.0D;
    double y = 68.1223D;
    double z = 1254.785D;
    TagLocation tagLocation = TagLocation.of(worldName, x, y, z);

    // When
    Tag tag = Tag.of(uuid, localDateTime, isEphemeral, tagLocation);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertEquals(uuid, tag.getUuid()),
        () -> assertEquals(localDateTime, tag.getInitLocalDateTime()),
        () -> assertEquals(isEphemeral, tag.isEphemeral()),
        () -> assertEquals(tagLocation, tag.getTagLocation()));
  }

  /**
   * Verification of {@link Tag#equals(Object)} and {@link Tag#hashCode()}
   * implementations.
   */
  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(Tag.class).verify();
  }

  /**
   * Verification of {@link Tag#toString()} implementation.
   */
  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(Tag.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
