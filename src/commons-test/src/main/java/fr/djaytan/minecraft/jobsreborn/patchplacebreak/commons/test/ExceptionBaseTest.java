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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
public abstract class ExceptionBaseTest {

  @InjectSoftAssertions private SoftAssertions softly;

  protected abstract @NotNull Exception getException();

  protected abstract @NotNull Exception getException(@NotNull String message);

  protected abstract @NotNull Exception getException(@Nullable Throwable cause);

  protected abstract @NotNull Exception getException(
      @NotNull String message, @Nullable Throwable cause);

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("Without args")
    @SuppressWarnings("java:S100")
    void withoutArgs_shouldMetExpectations() {
      // Given

      // When
      Exception exception = getException();

      // Then
      softly.assertThat(exception.getMessage()).isNull();
      softly.assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("With message")
    @SuppressWarnings("java:S100")
    void withoutMessage_shouldMetExpectations() {
      // Given
      String message = "message1";

      // When
      Exception exception = getException(message);

      // Then
      softly.assertThat(exception.getMessage()).isEqualTo(message);
      softly.assertThat(exception.getCause()).isNull();
    }

    @Nested
    @DisplayName("With cause")
    class WithCause {

      @Test
      @DisplayName("Having message")
      @SuppressWarnings("java:S100")
      void havingMessage_shouldHaveCauseAndMessageDefined() {
        // Given
        Throwable cause = new IllegalArgumentException("Cause message");

        // When
        Exception exception = getException(cause);

        // Then
        softly.assertThat(exception.getMessage()).isEqualTo("Cause message");
        softly.assertThat(exception.getCause()).isEqualTo(cause);
      }

      @Test
      @DisplayName("Not having message")
      @SuppressWarnings("java:S100")
      void notHavingMessage_shouldHaveOnlyCauseDefined() {
        // Given
        Throwable cause = new IllegalArgumentException();

        // When
        Exception exception = getException(cause);

        // Then
        softly.assertThat(exception.getMessage()).isNull();
        softly.assertThat(exception.getCause()).isEqualTo(cause);
      }

      @Test
      @DisplayName("Being null")
      @SuppressWarnings("java:S100")
      void beingNull_shouldHaveNothingDefined() {
        // Given

        // When
        Exception exception = getException((Throwable) null);

        // Then
        softly.assertThat(exception.getMessage()).isNull();
        softly.assertThat(exception.getCause()).isNull();
      }
    }

    @Nested
    @DisplayName("With message & cause")
    class WithMessageAndCause {

      @Test
      @DisplayName("And cause not null")
      @SuppressWarnings("java:S100")
      void andCauseNotNull_shouldMetExpectations() {
        // Given
        String message = "message2";
        Throwable cause = new IllegalArgumentException();

        // When
        Exception exception = getException(message, cause);

        // Then
        softly.assertThat(exception.getMessage()).isEqualTo(message);
        softly.assertThat(exception.getCause()).isEqualTo(cause);
      }

      @Test
      @DisplayName("And cause null")
      @SuppressWarnings("java:S100")
      void andCauseNull_shouldMetExpectations() {
        // Given
        String message = "message3";

        // When
        Exception exception = getException(message, null);

        // Then
        softly.assertThat(exception.getMessage()).isEqualTo(message);
        softly.assertThat(exception.getCause()).isNull();
      }
    }
  }
}
