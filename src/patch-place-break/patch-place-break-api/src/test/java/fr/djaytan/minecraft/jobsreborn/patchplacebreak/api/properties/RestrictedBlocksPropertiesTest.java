package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RestrictedBlocksPropertiesTest {

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("With nominal values")
    void withNominalValues() {
      // Given
      Set<String> materials = new HashSet<>(Arrays.asList("COBBLESTONE", "STONE", "DIAMOND_BLOCK"));
      RestrictionMode mode = RestrictionMode.WHITELIST;

      // When
      RestrictedBlocksProperties restrictedBlocksProperties =
          RestrictedBlocksProperties.of(materials, mode);

      // Then
      assertAll(
          "Verification of returned values from getters",
          () -> assertThat(restrictedBlocksProperties.getMaterials()).isEqualTo(materials),
          () -> assertThat(restrictedBlocksProperties.getMode()).isEqualTo(mode));
    }

    @Nested
    @DisplayName("For immutability")
    class ForImmutability {

      @Test
      @DisplayName("With element added from initial set")
      void withElementAddedFromInitialSet() {
        // Given
        Set<String> materials =
            new HashSet<>(Arrays.asList("COBBLESTONE", "STONE", "DIAMOND_BLOCK"));
        RestrictionMode mode = RestrictionMode.WHITELIST;

        RestrictedBlocksProperties restrictedBlocksProperties =
            RestrictedBlocksProperties.of(materials, mode);

        // When
        materials.add("SPONGE");

        // Then
        assertThat(restrictedBlocksProperties.getMaterials()).isNotEqualTo(materials);
      }

      @Test
      @DisplayName("With element added from property set")
      void withElementAddedFromPropertySet() {
        // Given
        Set<String> materials =
            new HashSet<>(Arrays.asList("COBBLESTONE", "STONE", "DIAMOND_BLOCK"));
        RestrictionMode mode = RestrictionMode.WHITELIST;

        RestrictedBlocksProperties restrictedBlocksProperties =
            RestrictedBlocksProperties.of(materials, mode);

        // When
        ThrowableAssert.ThrowingCallable throwingCallable =
            () -> restrictedBlocksProperties.getMaterials().add("SPONGE");

        // Then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(UnsupportedOperationException.class);
      }
    }
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(RestrictedBlocksProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(RestrictedBlocksProperties.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
