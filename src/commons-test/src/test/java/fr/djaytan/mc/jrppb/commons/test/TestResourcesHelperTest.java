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
package fr.djaytan.mc.jrppb.commons.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TestResourcesHelperTest {

  @Nested
  class WhenRetrievingClassResource {

    @Nested
    class AsAbsolutePath {

      @Test
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
                        "fr/djaytan/mc/jrppb/commons/test/TestResourcesHelperTest/coffee.txt")))
            .isTrue();
      }

      @Test
      void withoutExistingResource_shouldThrowException() {
        // Given
        Class<?> clazz = TestResourcesHelperTest.class;
        String resourceName = "notExistingResource";

        // When
        Exception exception =
            catchException(
                () -> TestResourcesHelper.getClassResourceAsAbsolutePath(clazz, resourceName));

        // Then
        assertThat(exception)
            .isExactlyInstanceOf(UncheckedIOException.class)
            .hasMessageContaining(
                Paths.get(
                        "fr/djaytan/mc/jrppb/commons/test/TestResourcesHelperTest/notExistingResource")
                    .toString());
      }
    }

    @Nested
    class AsString {

      @Nested
      class WithExistingResource {

        @Test
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
        void andNotChopped_shouldReturnExpectedContent() {
          // Given
          Class<?> clazz = TestResourcesHelperTest.class;
          String resourceName = "coffee.txt";

          // When
          String resourceContent =
              TestResourcesHelper.getClassResourceAsString(clazz, resourceName, false);

          // Then
          assertThat(resourceContent).isEqualToIgnoringNewLines("Taking a coffee");
        }
      }

      @Test
      void withoutExistingResource_shouldThrowException() {
        // Given
        Class<?> clazz = TestResourcesHelperTest.class;
        String resourceName = "notExistingResource";

        // When
        Exception exception =
            catchException(
                () -> TestResourcesHelper.getClassResourceAsString(clazz, resourceName, true));

        // Then
        assertThat(exception)
            .isExactlyInstanceOf(UncheckedIOException.class)
            .hasMessageContaining(
                Paths.get(
                        "fr/djaytan/mc/jrppb/commons/test/TestResourcesHelperTest/notExistingResource")
                    .toString());
      }
    }
  }
}
