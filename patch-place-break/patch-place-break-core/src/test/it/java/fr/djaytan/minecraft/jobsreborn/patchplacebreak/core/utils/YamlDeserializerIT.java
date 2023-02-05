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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;
import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.spongepowered.configurate.serialize.SerializationException;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.annotated.DbmsHostValidatingProperties;

class YamlDeserializerIT {

  private YamlDeserializer yamlDeserializer = new YamlDeserializer();

  @BeforeEach
  void beforeEach() {
    yamlDeserializer = new YamlDeserializer();
  }

  @Nested
  @DisplayName("When deserializing")
  class WhenDeserializing {

    @Test
    @DisplayName("With nominal values")
    void withNominalValues_shouldSuccess() {
      // Given
      String yamlFileName = "whenDeserializing_withNominalValues.yml";
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingSupplier<Optional<DbmsHostValidatingProperties>> executable =
          () -> yamlDeserializer.deserialize(yamlFile, DbmsHostValidatingProperties.class);

      // Then
      Optional<DbmsHostValidatingProperties> optionalHostValidatingProperties =
          assertDoesNotThrow(executable);
      assertThat(optionalHostValidatingProperties).isPresent().get()
          .isEqualTo(DbmsHostValidatingProperties.of("example.com", 1234, true));
    }

    @Test
    @DisplayName("With camelCase fields")
    void withCamelCaseFields_shouldSuccess() {
      // Given
      String yamlFileName = "whenDeserializing_withCamelCaseFields.yml";
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingSupplier<Optional<DbmsHostValidatingProperties>> executable =
          () -> yamlDeserializer.deserialize(yamlFile, DbmsHostValidatingProperties.class);

      // Then
      Optional<DbmsHostValidatingProperties> optionalHostValidatingProperties =
          assertDoesNotThrow(executable);
      assertThat(optionalHostValidatingProperties).isPresent().get()
          .isEqualTo(DbmsHostValidatingProperties.of("example.com", 1234, true));
    }

    @Test
    @DisplayName("With kebab-case fields")
    void withKebabCaseFields_shouldNonCamelCaseCompatibleFieldsIgnored() {
      // Given
      String yamlFileName = "whenDeserializing_withKebabCaseFields.yml";
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingCallable throwingCallable =
          () -> yamlDeserializer.deserialize(yamlFile, DbmsHostValidatingProperties.class);


      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(SerializationException.class)
          .hasMessageContaining("isSslEnabled");
    }
  }
}
