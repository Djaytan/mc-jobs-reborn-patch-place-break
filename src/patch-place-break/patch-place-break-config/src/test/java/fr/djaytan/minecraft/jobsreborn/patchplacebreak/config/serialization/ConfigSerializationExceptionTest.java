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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S2187")
class ConfigSerializationExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NotNull Exception getException() {
    return new ConfigSerializationException();
  }

  @Override
  protected @NotNull Exception getException(@NotNull String message) {
    return new ConfigSerializationException(message);
  }

  @Override
  protected @NotNull Exception getException(@Nullable Throwable cause) {
    return new ConfigSerializationException(cause);
  }

  @Override
  protected @NotNull Exception getException(@NotNull String message, @Nullable Throwable cause) {
    return new ConfigSerializationException(message, cause);
  }

  @Test
  @DisplayName("When instantiating serialization exception")
  void whenInstantiatingSerializationException() {
    // Given
    Class<?> propertiesType = DataSourceProperties.class;
    Path destConfigFile = Paths.get("data/folder/dataSource.conf");
    Throwable cause = new IOException();

    // When
    ConfigSerializationException configSerializationException =
        ConfigSerializationException.serialization(destConfigFile, propertiesType, cause);

    // Then
    assertThat(configSerializationException)
        .hasCause(cause)
        .hasMessageMatching(
            "Fail to serialize config properties of type "
                + "'fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties'"
                + " from '.*' file")
        .hasMessageContaining(destConfigFile.toString())
        .hasMessageContaining("dataSource.conf");
  }

  @Test
  @DisplayName("When instantiating deserialization exception")
  void whenInstantiatingDeserializationException() {
    // Given
    Class<?> propertiesType = DataSourceProperties.class;
    Path srcConfigFile = Paths.get("data/folder/dataSource.conf");
    Throwable cause = new IOException();

    // When
    ConfigSerializationException configSerializationException =
        ConfigSerializationException.deserialization(srcConfigFile, propertiesType, cause);

    // Then
    assertThat(configSerializationException)
        .hasCause(cause)
        .hasMessageMatching(
            "Fail to deserialize config properties of type "
                + "'fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties'"
                + " from '.*' file")
        .hasMessageContaining(srcConfigFile.toString())
        .hasMessageContaining("dataSource.conf");
  }

  @Test
  @DisplayName("When instantiating invalid loader configuration exception")
  void whenInstantiatingInvalidLoaderConfigurationException() {
    // Given

    // When
    ConfigSerializationException configSerializationException =
        ConfigSerializationException.invalidLoaderConfiguration();

    // Then
    assertThat(configSerializationException)
        .hasMessage(
            "The loader configuration is invalid and thus prevent processing "
                + "serializations and deserializations of config files");
  }
}
