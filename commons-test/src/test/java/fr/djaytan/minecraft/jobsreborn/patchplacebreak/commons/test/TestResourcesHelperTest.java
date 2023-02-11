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
        assertThat(path.endsWith(Paths.get(
            "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/TestResourcesHelperTest/coffee.txt")))
            .isTrue();
      }

      @Test
      @DisplayName("Without existing resource")
      void withoutExistingResource_shouldThrowIOException() {
        // Given
        Class<?> clazz = TestResourcesHelperTest.class;
        String resourceName = "notExistingResource";

        // When
        ThrowingCallable throwingCallable =
            () -> TestResourcesHelper.getClassResourceAsAbsolutePath(clazz, resourceName);

        // Then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(IOException.class)
            .hasMessageContaining(Paths.get(
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
      void withoutExistingResource_shouldThrowIOException() {
        // Given
        Class<?> clazz = TestResourcesHelperTest.class;
        String resourceName = "notExistingResource";

        // When
        ThrowingCallable throwingCallable =
            () -> TestResourcesHelper.getClassResourceAsString(clazz, resourceName, true);

        // Then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(IOException.class)
            .hasMessageContaining(Paths.get(
                "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/TestResourcesHelperTest/notExistingResource")
                .toString());
      }
    }
  }
}
