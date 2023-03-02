package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import lombok.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class ExceptionBaseTest {

  protected abstract @NonNull Exception getException();

  protected abstract @NonNull Exception getException(@NonNull String message);

  protected abstract @NonNull Exception getException(Throwable cause);

  protected abstract @NonNull Exception getException(@NonNull String message, Throwable cause);

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
      assertAll(
          () -> assertThat(exception.getMessage()).isNull(),
          () -> assertThat(exception.getCause()).isNull());
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
      assertAll(
          () -> assertThat(exception.getMessage()).isEqualTo(message),
          () -> assertThat(exception.getCause()).isNull());
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
        assertAll(
            () -> assertThat(exception.getMessage()).isEqualTo("Cause message"),
            () -> assertThat(exception.getCause()).isEqualTo(cause));
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
        assertAll(
            () -> assertThat(exception.getMessage()).isNull(),
            () -> assertThat(exception.getCause()).isEqualTo(cause));
      }

      @Test
      @DisplayName("Being null")
      @SuppressWarnings("java:S100")
      void beingNull_shouldHaveNothingDefined() {
        // Given

        // When
        Exception exception = getException((Throwable) null);

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
      @SuppressWarnings("java:S100")
      void andCauseNotNull_shouldMetExpectations() {
        // Given
        String message = "message2";
        Throwable cause = new IllegalArgumentException();

        // When
        Exception exception = getException(message, cause);

        // Then
        assertAll(
            () -> assertThat(exception.getMessage()).isEqualTo(message),
            () -> assertThat(exception.getCause()).isEqualTo(cause));
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
        assertAll(
            () -> assertThat(exception.getMessage()).isEqualTo(message),
            () -> assertThat(exception.getCause()).isNull());
      }
    }
  }
}
