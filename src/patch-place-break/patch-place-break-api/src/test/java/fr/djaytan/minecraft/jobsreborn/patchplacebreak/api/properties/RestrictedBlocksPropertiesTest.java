/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.ThrowableAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
          new RestrictedBlocksProperties(materials, mode);

      // Then
      assertAll(
          "Verification of returned values from getters",
          () -> assertThat(restrictedBlocksProperties.getMaterials()).isEqualTo(materials),
          () -> assertThat(restrictedBlocksProperties.getRestrictionMode()).isEqualTo(mode));
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
            new RestrictedBlocksProperties(materials, mode);

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
            new RestrictedBlocksProperties(materials, mode);

        // When
        ThrowableAssert.ThrowingCallable throwingCallable =
            () -> restrictedBlocksProperties.getMaterials().add("SPONGE");

        // Then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(UnsupportedOperationException.class);
      }
    }
  }

  @ParameterizedTest(name = "{index} - With {0} restriction mode")
  @MethodSource
  @DisplayName("When checking block restriction")
  void whenCheckingBlockRestriction(
      @NotNull RestrictionMode restrictionMode,
      boolean isListedMaterialExpectedToBeRestricted,
      boolean isNonListedMaterialExpectedToBeRestricted) {
    // Given
    String listedMaterial = "STONE";
    String nonListedMaterial = "BEACON";
    RestrictedBlocksProperties restrictedBlocksProperties =
        new RestrictedBlocksProperties(Arrays.asList(listedMaterial), restrictionMode);

    // When
    boolean isListedMaterialRestricted = restrictedBlocksProperties.isRestricted(listedMaterial);
    boolean isNonListedMaterialRestricted =
        restrictedBlocksProperties.isRestricted(nonListedMaterial);

    // Then
    assertAll(
        () ->
            assertThat(isListedMaterialRestricted)
                .isEqualTo(isListedMaterialExpectedToBeRestricted),
        () ->
            assertThat(isNonListedMaterialRestricted)
                .isEqualTo(isNonListedMaterialExpectedToBeRestricted));
  }

  private static @NotNull Stream<Arguments> whenCheckingBlockRestriction() {
    return Stream.of(
        arguments(named(RestrictionMode.BLACKLIST.name(), RestrictionMode.BLACKLIST), true, false),
        arguments(named(RestrictionMode.WHITELIST.name(), RestrictionMode.WHITELIST), false, true),
        arguments(named(RestrictionMode.DISABLED.name(), RestrictionMode.DISABLED), false, false));
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
