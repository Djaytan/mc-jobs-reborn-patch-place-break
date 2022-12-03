package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class TagLocationTest {

  /**
   * Given required data to create a TagLocation,
   * When calling the constructor with these data,
   * Then the TagLocation is created successfully.
   */
  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    String worldName = "world";
    double x = -54.0D;
    double y = 67.785D;
    double z = 4872.45152D;

    // When
    TagLocation tagLocation = TagLocation.of(worldName, x, y, z);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertEquals(worldName, tagLocation.getWorldName()),
        () -> assertEquals(x, tagLocation.getX()), () -> assertEquals(y, tagLocation.getY()),
        () -> assertEquals(z, tagLocation.getZ()));
  }

  /**
   * Verification of {@link TagLocation#equals(Object)} and {@link TagLocation#hashCode()}
   * implementations.
   */
  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void shouldEqualsAndHashcodeWellImplemented() {
    EqualsVerifier.forClass(TagLocation.class).verify();
  }

  /**
   * Verification of {@link TagLocation#toString()} implementation.
   */
  @Test
  @DisplayName("toString() - Verifications")
  void shouldToStringWellImplemented() {
    ToStringVerifier.forClass(TagLocation.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
