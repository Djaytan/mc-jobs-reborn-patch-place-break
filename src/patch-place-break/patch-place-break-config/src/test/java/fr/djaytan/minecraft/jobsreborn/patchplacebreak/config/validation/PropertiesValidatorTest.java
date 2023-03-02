package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.ConnectionPoolValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.CredentialsValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DataSourceValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DbmsHostValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DbmsServerValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsServerProperties;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PropertiesValidatorTest {

  private PropertiesValidator propertiesValidator;

  @BeforeEach
  void beforeEach() {
    propertiesValidator = new PropertiesValidator(ValidatorTestWrapper.getValidator());
  }

  @Test
  @DisplayName("When validating with valid values")
  void whenValidating_withValidValues_shouldSuccess() {
    // Given
    DataSourceValidatingProperties dataSourceValidatingProperties =
        DataSourceValidatingProperties.of(
            DataSourceType.MYSQL,
            "patch_place_break",
            DbmsServerValidatingProperties.of(
                DbmsHostValidatingProperties.of("example.com", 1234, true),
                CredentialsValidatingProperties.of("foo", "bar"),
                "patch_database"),
            ConnectionPoolValidatingProperties.of(60000, 10));

    // When
    DataSourceProperties dataSourceProperties =
        propertiesValidator.validate(dataSourceValidatingProperties);

    // Then
    assertAll(
        () -> assertThat(dataSourceValidatingProperties.isValidated()).isTrue(),
        () ->
            assertThat(dataSourceProperties)
                .isEqualTo(
                    DataSourceProperties.of(
                        DataSourceType.MYSQL,
                        "patch_place_break",
                        DbmsServerProperties.of(
                            DbmsHostProperties.of("example.com", 1234, true),
                            CredentialsProperties.of("foo", "bar"),
                            "patch_database"),
                        ConnectionPoolProperties.of(60000, 10))));
  }

  @Test
  @DisplayName("When validating with invalid values")
  void whenValidating_withInvalidValues_shouldThrowException() {
    // Given
    DataSourceValidatingProperties dataSourceValidatingProperties =
        DataSourceValidatingProperties.of(
            DataSourceType.MYSQL,
            "patch_place_break",
            DbmsServerValidatingProperties.of(
                DbmsHostValidatingProperties.of("example.com", -1, true),
                CredentialsValidatingProperties.of("foo", "bar"),
                "patch_database"),
            ConnectionPoolValidatingProperties.of(60000, 10));

    // When
    ThrowingCallable throwingCallable =
        () -> propertiesValidator.validate(dataSourceValidatingProperties);

    // Then
    assertThatThrownBy(throwingCallable).isExactlyInstanceOf(PropertiesValidationException.class);
  }
}
