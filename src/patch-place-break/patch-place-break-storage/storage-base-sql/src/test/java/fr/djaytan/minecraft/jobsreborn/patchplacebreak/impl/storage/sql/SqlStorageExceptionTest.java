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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SqlStorageExceptionTest {

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("Without args")
    void withoutArgs_shouldMetExpectations() {
      // Given

      // When
      SqlStorageException exception = new SqlStorageException();

      // Then
      assertAll(
          () -> assertThat(exception.getMessage()).isNull(),
          () -> assertThat(exception.getCause()).isNull());
    }

    @Test
    @DisplayName("With message")
    void withoutMessage_shouldMetExpectations() {
      // Given
      String message = "A message";

      // When
      SqlStorageException exception = new SqlStorageException(message);

      // Then
      assertAll(
          () -> assertThat(exception.getMessage()).isEqualTo(message),
          () -> assertThat(exception.getCause()).isNull());
    }

    @Nested
    @DisplayName("With cause")
    class WithCause {

      @Test
      @DisplayName("Having message")
      void havingMessage_shouldHaveCauseAndMessageDefined() {
        // Given
        Throwable cause = new IllegalArgumentException("Cause message");

        // When
        SqlStorageException exception = new SqlStorageException(cause);

        // Then
        assertAll(
            () -> assertThat(exception.getMessage()).isEqualTo("Cause message"),
            () -> assertThat(exception.getCause()).isEqualTo(cause));
      }

      @Test
      @DisplayName("Not having message")
      void notHavingMessage_shouldHaveOnlyCauseDefined() {
        // Given
        Throwable cause = new IllegalArgumentException();

        // When
        SqlStorageException exception = new SqlStorageException(cause);

        // Then
        assertAll(
            () -> assertThat(exception.getMessage()).isNull(),
            () -> assertThat(exception.getCause()).isEqualTo(cause));
      }

      @Test
      @DisplayName("Being null")
      void beingNull_shouldHaveNothingDefined() {
        // Given

        // When
        SqlStorageException exception = new SqlStorageException((Throwable) null);

        // Then
        assertAll(
            () -> assertThat(exception.getMessage()).isNull(),
            () -> assertThat(exception.getCause()).isNull());
      }
    }

    @Nested
    @DisplayName("With message & cause")
    class WithMessageAndCause {

      @Test
      @DisplayName("And cause not null")
      void andCauseNotNull_shouldMetExpectations() {
        // Given
        String message = "A message";
        Throwable cause = new IllegalArgumentException();

        // When
        SqlStorageException exception = new SqlStorageException(message, cause);

        // Then
        assertAll(
            () -> assertThat(exception.getMessage()).isEqualTo(message),
            () -> assertThat(exception.getCause()).isEqualTo(cause));
      }

      @Test
      @DisplayName("And cause null")
      void andCauseNull_shouldMetExpectations() {
        // Given
        String message = "A message";

        // When
        SqlStorageException exception = new SqlStorageException(message, null);

        // Then
        assertAll(
            () -> assertThat(exception.getMessage()).isEqualTo(message),
            () -> assertThat(exception.getCause()).isNull());
      }
    }
  }
}
