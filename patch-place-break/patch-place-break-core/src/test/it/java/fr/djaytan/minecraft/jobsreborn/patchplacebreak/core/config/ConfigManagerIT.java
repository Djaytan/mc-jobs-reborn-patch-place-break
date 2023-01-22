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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config;

import static fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.ConfigManager.CONFIG_FILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.validation.PropertiesValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.configurate.serialize.SerializationException;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.validation.PropertiesValidator;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.utils.YamlDeserializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;

@ExtendWith(MockitoExtension.class)
class ConfigManagerIT {

  @Mock
  private ClassLoader classLoaderMocked;
  private ConfigManager configManager;
  private FileSystem fileSystem;
  private Path configFile;

  @BeforeEach
  void beforeEach() {
    fileSystem = Jimfs.newFileSystem(Configuration.unix());

    configFile = fileSystem.getPath(CONFIG_FILE_NAME);
    PropertiesValidator propertiesValidator =
        new PropertiesValidator(ValidatorTestWrapper.getValidator());
    YamlDeserializer yamlDeserializer = new YamlDeserializer();
    configManager =
        new ConfigManager(classLoaderMocked, configFile, propertiesValidator, yamlDeserializer);
  }

  @AfterEach
  void afterEach() throws IOException {
    fileSystem.close();
  }

  @Nested
  @DisplayName("When creating config")
  class WhenCreatingConfig {

    @Test
    @DisplayName("With existing one")
    void withExistingConfigFile_shouldDoNothing() throws IOException {
      // Given
      Files.createFile(configFile);

      // When
      configManager.createIfNotExists();

      // Then
      Mockito.verifyNoInteractions(classLoaderMocked);
    }

    @Test
    @DisplayName("With IOException thrown")
    void withIOExceptionThrown_shouldThrowConfigException() throws IOException {
      // Given
      String defaultConfigFileName = "whenCreatingConfig_withIOExceptionThrown.yml";
      Path defaultConfigFile = TestResourcesHelper.getResourceFromTestClassAsPath(this.getClass(),
          defaultConfigFileName);

      given(classLoaderMocked.getResourceAsStream(anyString()))
          .willReturn(Files.newInputStream(defaultConfigFile));

      try (MockedStatic<Files> mockedStatic = mockStatic(Files.class)) {
        mockedStatic.when(() -> Files.copy(any(InputStream.class), eq(configFile)))
            .thenThrow(new IOException());

        // When
        ThrowingCallable throwingCallable = () -> configManager.createIfNotExists();

        // Then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class)
            .hasMessageContaining(CONFIG_FILE_NAME)
            .hasRootCauseExactlyInstanceOf(IOException.class);
      }
    }

    @Nested
    @DisplayName("Without existing one")
    class WithoutExistingOne {

      @Test
      @DisplayName("And without default one")
      void andWithoutDefaultConfigFile_shouldThrowConfigException() {
        // Given
        given(classLoaderMocked.getResourceAsStream(anyString())).willReturn(null);

        // When
        ThrowingCallable throwingCallable = () -> configManager.createIfNotExists();

        // Then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class)
            .hasMessageContaining(CONFIG_FILE_NAME).hasNoCause();
      }

      @Nested
      @DisplayName("And with default one")
      class AndWithDefaultOne {

        @Test
        @DisplayName("Being not empty")
        void beingNotEmpty_thenShouldNotThrow() throws IOException {
          // Given
          String defaultConfigFileName =
              "whenCreatingConfig_withoutExistingOne_andWithDefaultOne_beingNotEmpty.yml";
          Path defaultConfigFile = TestResourcesHelper
              .getResourceFromTestClassAsPath(this.getClass(), defaultConfigFileName);

          given(classLoaderMocked.getResourceAsStream(anyString()))
              .willReturn(Files.newInputStream(defaultConfigFile));

          // When
          ThrowingCallable throwingCallable = () -> configManager.createIfNotExists();

          // Then
          assertThatCode(throwingCallable).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Being empty")
        void beingEmpty_thenShouldThrowConfigException() throws IOException {
          // Given
          String defaultConfigFileName =
              "whenCreatingConfig_withoutExistingOne_andWithDefaultOne_beingEmpty.yml";
          Path defaultConfigFile = TestResourcesHelper
              .getResourceFromTestClassAsPath(this.getClass(), defaultConfigFileName);

          given(classLoaderMocked.getResourceAsStream(anyString()))
              .willReturn(Files.newInputStream(defaultConfigFile));

          // When
          ThrowingCallable throwingCallable = () -> configManager.createIfNotExists();

          // Then
          assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class)
              .hasMessageContaining(CONFIG_FILE_NAME).hasNoCause();
        }
      }
    }
  }

  @Nested
  @DisplayName("When reading and validating config")
  class WhenReadingAndValidatingConfig {

    @Test
    @DisplayName("With nominal config file")
    void withNominalConfigFile_shouldSuccess() throws IOException {
      // Given
      String nominalConfigFileName = "whenReadingAndValidatingConfig_withNominalConfigFile.yml";
      Path nominalConfigFile = TestResourcesHelper.getResourceFromTestClassAsPath(this.getClass(),
          nominalConfigFileName);
      Files.copy(nominalConfigFile, configFile);

      // When
      ConfigProperties configProperties = configManager.readAndValidate();

      // Then
      assertThat(configProperties.getDataSource())
          .isEqualTo(DataSourceProperties.of(DataSourceType.SQLITE, "patch_place_break_tag",
              DbmsServerProperties.of(DbmsHostProperties.of("localhost", 3306, true),
                  CredentialsProperties.of("username", "password"), "database"),
              ConnectionPoolProperties.of(30000, 10)));
    }

    @Test
    @DisplayName("With missing required fields in config file")
    void withMissingRequiredFieldsInConfigFile_shouldThrowConfigException() throws IOException {
      // Given
      String invalidConfigFileName =
          "whenReadingAndValidatingConfig_withMissingRequiredFieldsInConfigFile.yml";
      Path invalidConfigFile = TestResourcesHelper.getResourceFromTestClassAsPath(this.getClass(),
          invalidConfigFileName);
      Files.copy(invalidConfigFile, configFile);

      // When
      ThrowingCallable throwingCallable = () -> configManager.readAndValidate();

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    @Test
    @DisplayName("With invalid config file")
    void withInvalidConfigFile_shouldThrowPropertiesValidationException() throws IOException {
      // Given
      String invalidConfigFileName = "whenReadingAndValidatingConfig_withInvalidConfigFile.yml";
      Path invalidConfigFile = TestResourcesHelper.getResourceFromTestClassAsPath(this.getClass(),
          invalidConfigFileName);
      Files.copy(invalidConfigFile, configFile);

      // When
      ThrowingCallable throwingCallable = () -> configManager.readAndValidate();

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(PropertiesValidationException.class);
    }

    @Test
    @DisplayName("With empty config file")
    void withEmptyConfigFile_shouldThrowConfigException() throws IOException {
      // Given
      String emptyConfigFileName = "whenReadingAndValidatingConfig_withEmptyConfigFile.yml";
      Path emptyConfigFile =
          TestResourcesHelper.getResourceFromTestClassAsPath(this.getClass(), emptyConfigFileName);
      Files.copy(emptyConfigFile, configFile);

      // When
      ThrowingCallable throwingCallable = () -> configManager.readAndValidate();

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class).hasNoCause();
    }

    @Test
    @DisplayName("Without existing config file")
    void withoutExistingConfigFile_shouldThrowConfigException() {
      // Given

      // When
      ThrowingCallable throwingCallable = () -> configManager.readAndValidate();

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class).hasNoCause();
    }
  }
}
