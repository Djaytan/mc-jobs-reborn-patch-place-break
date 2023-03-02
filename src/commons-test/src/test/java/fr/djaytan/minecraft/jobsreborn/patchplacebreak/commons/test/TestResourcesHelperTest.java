package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TestResourcesHelperTest {

  @Nested
  @DisplayName("When retrieving class resource")
  class WhenRetrievingClassResource {

    @Nested
    @DisplayName("As absolute path")
    class AsAbsolutePath {

      @Test
      @DisplayName("With existing resource")
      void withExistingResource_shouldReturnExpectedPath() {
        // Given
        Class<?> clazz = TestResourcesHelperTest.class;
        String coffeeResource = "coffee.txt";

        // When
        Path path = TestResourcesHelper.getClassResourceAsAbsolutePath(clazz, coffeeResource);

        // Then
        assertThat(
                path.endsWith(
                    Paths.get(
                        "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/TestResourcesHelperTest/coffee.txt")))
            .isTrue();
      }

      @Test
      @DisplayName("Without existing resource")
      void withoutExistingResource_shouldThrowException() {
        // Given
        Class<?> clazz = TestResourcesHelperTest.class;
        String resourceName = "notExistingResource";

        // When
        ThrowingCallable throwingCallable =
            () -> TestResourcesHelper.getClassResourceAsAbsolutePath(clazz, resourceName);

        // Then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IOException.class)
            .hasMessageContaining(
                Paths.get(
                        "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/TestResourcesHelperTest/notExistingResource")
                    .toString());
      }
    }

    @Nested
    @DisplayName("As string")
    class AsString {

      @Nested
      @DisplayName("With existing resource")
      class WithExistingResource {

        @Test
        @DisplayName("And chopped")
        void andChopped_shouldReturnExpectedContent() {
          // Given
          Class<?> clazz = TestResourcesHelperTest.class;
          String resourceName = "coffee.txt";

          // When
          String resourceContent =
              TestResourcesHelper.getClassResourceAsString(clazz, resourceName, true);

          // Then
          assertThat(resourceContent).isEqualTo("Taking a coffee");
        }

        @Test
        @DisplayName("And not chopped")
        void andNotChopped_shouldReturnExpectedContent() {
          // Given
          Class<?> clazz = TestResourcesHelperTest.class;
          String resourceName = "coffee.txt";

          // When
          String resourceContent =
              TestResourcesHelper.getClassResourceAsString(clazz, resourceName, false);

          // Then
          assertThat(resourceContent).isEqualTo("Taking a coffee\n");
        }
      }

      @Test
      @DisplayName("Without existing resource")
      void withoutExistingResource_shouldThrowException() {
        // Given
        Class<?> clazz = TestResourcesHelperTest.class;
        String resourceName = "notExistingResource";

        // When
        ThrowingCallable throwingCallable =
            () -> TestResourcesHelper.getClassResourceAsString(clazz, resourceName, true);

        // Then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IOException.class)
            .hasMessageContaining(
                Paths.get(
                        "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/TestResourcesHelperTest/notExistingResource")
                    .toString());
      }
    }
  }
}
