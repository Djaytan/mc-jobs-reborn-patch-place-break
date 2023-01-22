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

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ConnectionPoolValidatingPropertiesTest {

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(ConnectionPoolValidatingProperties.class).withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(ConnectionPoolValidatingProperties.class)
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
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          new ConnectionPoolValidatingProperties();

      // Then
      assertAll(
          () -> assertThat(connectionPoolValidatingProperties.getConnectionTimeout()).isZero(),
          () -> assertThat(connectionPoolValidatingProperties.getPoolSize()).isZero(),
          () -> assertThat(connectionPoolValidatingProperties.isValidated()).isFalse());
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      long connectionTimeout = 30000;
      int poolSize = 10;

      // When
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(connectionTimeout, poolSize);

      // Then
      assertAll(
          () -> assertThat(connectionPoolValidatingProperties.getConnectionTimeout())
              .isEqualTo(connectionTimeout),
          () -> assertThat(connectionPoolValidatingProperties.getPoolSize()).isEqualTo(poolSize),
          () -> assertThat(connectionPoolValidatingProperties.isValidated()).isFalse());
    }
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("With properties marked as validated")
    void withPropertiesMarkedAsValidated_shouldConvertSuccessfully() {
      // Given
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(60000, 10);
      connectionPoolValidatingProperties.markAsValidated();

      // When
      ConnectionPoolProperties connectionPoolProperties =
          connectionPoolValidatingProperties.convert();

      // Then
      assertAll(() -> assertThat(connectionPoolValidatingProperties.isValidated()).isTrue(),
          () -> assertThat(connectionPoolProperties.getConnectionTimeout()).isEqualTo(60000),
          () -> assertThat(connectionPoolProperties.getPoolSize()).isEqualTo(10));
    }

    @Test
    @DisplayName("With properties not marked as validated")
    void withPropertiesNotMarkedAsValidated_shouldThrowIllegalStateException() {
      // Given
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(60000, 10);

      // When
      ThrowingCallable throwingCallable = connectionPoolValidatingProperties::convert;

      // Then
      assertAll(() -> assertThat(connectionPoolValidatingProperties.isValidated()).isFalse(),
          () -> assertThatIllegalStateException().isThrownBy(throwingCallable)
              .withMessage("Properties must be validated before being converted"));
    }
  }
}
