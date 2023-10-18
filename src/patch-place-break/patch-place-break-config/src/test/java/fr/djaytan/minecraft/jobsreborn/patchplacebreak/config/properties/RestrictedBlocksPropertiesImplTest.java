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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictionMode;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ConfigSerializerTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import jakarta.validation.ConstraintViolation;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.spongepowered.configurate.serialize.SerializationException;

class RestrictedBlocksPropertiesImplTest {

  private FileSystem imfs;

  @BeforeEach
  void beforeEach() {
    imfs = Jimfs.newFileSystem(Configuration.unix());
  }

  @AfterEach
  void afterEach() throws IOException {
    imfs.close();
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(RestrictedBlocksPropertiesImpl.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(RestrictedBlocksPropertiesImpl.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

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
      RestrictedBlocksPropertiesImpl properties =
          new RestrictedBlocksPropertiesImpl(materials, mode);

      // Then
      assertAll(
          "Verification of returned values from getters",
          () -> assertThat(properties.getMaterials()).isEqualTo(materials),
          () -> assertThat(properties.getRestrictionMode()).isEqualTo(mode));
    }

    @Test
    @DisplayName("With default values")
    void withDefaultValues() {
      // Given

      // Where
      RestrictedBlocksPropertiesImpl properties = new RestrictedBlocksPropertiesImpl();

      // Then
      assertAll(
          () -> assertThat(properties.getMaterials()).isEmpty(),
          () -> assertThat(properties.getRestrictionMode()).isEqualTo(RestrictionMode.DISABLED));
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

        RestrictedBlocksPropertiesImpl properties =
            new RestrictedBlocksPropertiesImpl(materials, mode);

        // When
        materials.add("SPONGE");

        // Then
        assertThat(properties.getMaterials()).isNotEqualTo(materials);
      }

      @Test
      @DisplayName("With element added from property set")
      void withElementAddedFromPropertySet() {
        // Given
        Set<String> materials =
            new HashSet<>(Arrays.asList("COBBLESTONE", "STONE", "DIAMOND_BLOCK"));
        RestrictionMode mode = RestrictionMode.WHITELIST;

        RestrictedBlocksPropertiesImpl properties =
            new RestrictedBlocksPropertiesImpl(materials, mode);

        // When
        Exception exception = catchException(() -> properties.getMaterials().add("SPONGE"));

        // Then
        assertThat(exception).isExactlyInstanceOf(UnsupportedOperationException.class);
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
    RestrictedBlocksPropertiesImpl properties =
        new RestrictedBlocksPropertiesImpl(List.of(listedMaterial), restrictionMode);

    // When
    boolean isListedMaterialRestricted = properties.isRestricted(listedMaterial);
    boolean isNonListedMaterialRestricted = properties.isRestricted(nonListedMaterial);

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

  @Nested
  @DisplayName("When validating")
  class WhenValidating {

    @Test
    @DisplayName("With default values")
    void withDefaultValues_shouldNotGenerateConstraintViolations() {
      // Given
      RestrictedBlocksPropertiesImpl properties = new RestrictedBlocksPropertiesImpl();

      // When
      Set<ConstraintViolation<RestrictedBlocksPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      RestrictedBlocksPropertiesImpl properties =
          new RestrictedBlocksPropertiesImpl(List.of("STONE"), RestrictionMode.BLACKLIST);

      // When
      Set<ConstraintViolation<RestrictedBlocksPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only invalid values")
    void withOnlyInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      RestrictedBlocksPropertiesImpl restrictedBlocksProperties =
          new RestrictedBlocksPropertiesImpl(Collections.emptyList(), null);

      // When
      Set<ConstraintViolation<RestrictedBlocksPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(restrictedBlocksProperties);

      // Then
      assertThat(constraintViolations).hasSize(1);
    }
  }

  @Nested
  @DisplayName("When serializing to YAML")
  @TestInstance(Lifecycle.PER_CLASS)
  class WhenSerializingToYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With valid values")
    void withValidValues_shouldMatchExpectedYamlContent(
        @NotNull RestrictedBlocksPropertiesImpl givenValue, @NotNull String expectedYamlFileName)
        throws IOException {
      // Given
      Path imDestFile = imfs.getPath("test.conf");

      // When
      ConfigSerializerTestWrapper.serialize(imDestFile, givenValue);

      // Then
      String actualYaml = new String(Files.readAllBytes(imDestFile));
      String expectedYaml =
          TestResourcesHelper.getClassResourceAsString(
              this.getClass(), expectedYamlFileName, false);
      assertThat(actualYaml).containsIgnoringNewLines(expectedYaml);
    }

    private @NotNull Stream<Arguments> withValidValues_shouldMatchExpectedYamlContent() {
      return Stream.of(
          arguments(
              named("With default values", new RestrictedBlocksPropertiesImpl()),
              "whenSerializing_withDefaultValues.conf"),
          arguments(
              named(
                  "With custom values",
                  new RestrictedBlocksPropertiesImpl(List.of("STONE"), RestrictionMode.BLACKLIST)),
              "whenSerializing_withCustomValues.conf"));
    }
  }

  @Nested
  @DisplayName("When deserializing from YAML")
  @TestInstance(Lifecycle.PER_CLASS)
  class WhenDeserializingFromYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With valid content")
    void withValidContent_shouldMatchExpectedValue(
        @NotNull String confFileName, @NotNull RestrictedBlocksPropertiesImpl expectedValue) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<RestrictedBlocksPropertiesImpl> optionalRestrictedBlocksPropertiesImpl =
          ConfigSerializerTestWrapper.deserialize(confFile, RestrictedBlocksPropertiesImpl.class);

      // Then
      assertThat(optionalRestrictedBlocksPropertiesImpl).hasValue(expectedValue);
    }

    private @NotNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          arguments(
              named("With valid values", "whenDeserializing_withValidValues.conf"),
              new RestrictedBlocksPropertiesImpl(List.of("STONE"), RestrictionMode.BLACKLIST)),
          arguments(
              named("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              new RestrictedBlocksPropertiesImpl(List.of("STONE"), RestrictionMode.BLACKLIST)));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowException(@NotNull String confFileName) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Exception exception =
          catchException(
              () ->
                  ConfigSerializerTestWrapper.deserialize(
                      confFile, RestrictedBlocksPropertiesImpl.class));

      // Then
      assertThat(exception)
          .isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private @NotNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          arguments(
              named(
                  "With missing 'materials' field",
                  "whenDeserializing_withMissingMaterialsField.conf")),
          arguments(
              named(
                  "With missing 'restrictionMode' field",
                  "whenDeserializing_withMissingRestrictionModeField.conf")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String confFileName = "whenDeserializing_withEmptyContent.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<RestrictedBlocksPropertiesImpl> optionalRestrictedBlocksPropertiesImpl =
          ConfigSerializerTestWrapper.deserialize(confFile, RestrictedBlocksPropertiesImpl.class);

      // Then
      assertThat(optionalRestrictedBlocksPropertiesImpl).isNotPresent();
    }
  }
}
