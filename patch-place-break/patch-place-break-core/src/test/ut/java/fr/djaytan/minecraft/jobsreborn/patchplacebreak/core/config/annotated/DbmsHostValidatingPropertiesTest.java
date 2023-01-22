/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.annotated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsHostProperties;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class DbmsHostValidatingPropertiesTest {

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(DbmsHostValidatingProperties.class).withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(DbmsHostValidatingProperties.class)
        .withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("With no args constructor")
    void withNoArgsConstructor_shouldMatchDefaultValues() {
      // Given

      // When
      DbmsHostValidatingProperties dbmsHostValidatingProperties =
          new DbmsHostValidatingProperties();

      // Then
      assertAll(() -> assertThat(dbmsHostValidatingProperties.getHostname()).isNull(),
          () -> assertThat(dbmsHostValidatingProperties.getPort()).isZero(),
          () -> assertThat(dbmsHostValidatingProperties.isSslEnabled()).isFalse(),
          () -> assertThat(dbmsHostValidatingProperties.isValidated()).isFalse());
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      String hostname = "example.com";
      int port = 1234;
      boolean isSslEnabled = true;

      // When
      DbmsHostValidatingProperties dbmsHostValidatingProperties =
          DbmsHostValidatingProperties.of(hostname, port, isSslEnabled);

      // Then
      assertAll(
          () -> assertThat(dbmsHostValidatingProperties.getHostname()).isEqualTo("example.com"),
          () -> assertThat(dbmsHostValidatingProperties.getPort()).isEqualTo(1234),
          () -> assertThat(dbmsHostValidatingProperties.isSslEnabled()).isTrue(),
          () -> assertThat(dbmsHostValidatingProperties.isValidated()).isFalse());
    }
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("With properties marked as validated")
    void withPropertiesMarkedAsValidated_shouldConvertSuccessfully() {
      // Given
      DbmsHostValidatingProperties dbmsHostValidatingProperties =
          DbmsHostValidatingProperties.of("example.com", 1234, true);
      dbmsHostValidatingProperties.markAsValidated();

      // When
      DbmsHostProperties dbmsHostProperties = dbmsHostValidatingProperties.convert();

      // Then
      assertAll(() -> assertThat(dbmsHostValidatingProperties.isValidated()).isTrue(),
          () -> assertThat(dbmsHostProperties.getHostname()).isEqualTo("example.com"),
          () -> assertThat(dbmsHostProperties.getPort()).isEqualTo(1234),
          () -> assertThat(dbmsHostProperties.isSslEnabled()).isTrue());
    }

    @Test
    @DisplayName("With properties not marked as validated")
    void withPropertiesNotMarkedAsValidated_shouldThrowIllegalStateException() {
      // Given
      DbmsHostValidatingProperties dbmsHostValidatingProperties =
          DbmsHostValidatingProperties.of("example.com", 1234, true);

      // When
      ThrowingCallable throwingCallable = dbmsHostValidatingProperties::convert;

      // Then
      assertAll(() -> assertThat(dbmsHostValidatingProperties.isValidated()).isFalse(),
          () -> assertThatIllegalStateException().isThrownBy(throwingCallable)
              .withMessage("Properties must be validated before being converted"));
    }
  }
}
