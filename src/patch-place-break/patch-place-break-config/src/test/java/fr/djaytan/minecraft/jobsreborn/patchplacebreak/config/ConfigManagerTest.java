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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DataSourceValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidator;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.ValidatingConvertibleProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;
import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class ConfigManagerTest {

  private ConfigManager configManager;
  @Spy
  private ConfigSerializer configSerializerSpied;
  private Path dataFolder;
  private FileSystem imfs;

  @BeforeEach
  void beforeEach() {
    imfs = Jimfs.newFileSystem(Configuration.unix());

    dataFolder = imfs.getPath("");
    PropertiesValidator propertiesValidator =
        new PropertiesValidator(ValidatorTestWrapper.getValidator());
    configManager = new ConfigManager(dataFolder, configSerializerSpied, propertiesValidator);
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    imfs.close();
  }

  @Nested
  @DisplayName("When creating default config")
  class WhenCreatingDefaultConfig {

    @Test
    @DisplayName("With already existing one")
    void withAlreadyExistingOne_shouldDoNothing() throws IOException {
      // Given
      String configFileName = "alreadyExisting.conf";
      DataSourceValidatingProperties defaultProperties = DataSourceValidatingProperties.ofDefault();
      Path configFile = dataFolder.resolve(configFileName);
      Files.createFile(configFile);

      // When
      configManager.createDefaultIfNotExists(configFileName, defaultProperties);

      // Then
      String configFileContent = new String(Files.readAllBytes(configFile));

      assertThat(configFileContent).isEmpty();
    }

    @Test
    @DisplayName("With generic default properties")
    @SneakyThrows
    void withGenericDefaultProperties_shouldSerializeConfig() {
      // Given
      String configFileName = "nominalCase.conf";
      ValidatingConvertibleProperties<?> defaultProperties =
          DataSourceValidatingProperties.ofDefault();

      // When
      configManager.createDefaultIfNotExists(configFileName, defaultProperties);

      // Then
      String expectedConfigContent = TestResourcesHelper.getClassResourceAsString(this.getClass(),
          "whenCreatingConfig_withGenericDefaultProperties.conf", false);
      Path configFile = dataFolder.resolve(configFileName);
      String actualConfigContent = new String(Files.readAllBytes(configFile));

      assertThat(actualConfigContent).containsIgnoringNewLines(expectedConfigContent);
    }

    @Test
    @DisplayName("With exception thrown at serialization time")
    void withExceptionThrownAtSerializationTime_shouldThrowWrapperException() {
      // Given
      String configFileName = "exception.conf";
      DataSourceValidatingProperties defaultProperties = DataSourceValidatingProperties.ofDefault();
      doThrow(ConfigSerializationException.class).when(configSerializerSpied).serialize(any(),
          any());

      // When
      ThrowingCallable throwingCallable =
          () -> configManager.createDefaultIfNotExists(configFileName, defaultProperties);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigSerializationException.class);
    }

    @Test
    @DisplayName("With invalid default properties")
    void withInvalidDefaultProperties_shouldThrowException() {
      // Given
      String configFileName = "exception.conf";
      DataSourceValidatingProperties defaultProperties =
          DataSourceValidatingProperties.of(null, null, null, null);

      // When
      ThrowingCallable throwingCallable =
          () -> configManager.createDefaultIfNotExists(configFileName, defaultProperties);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(PropertiesValidationException.class);
    }
  }

  @Nested
  @DisplayName("When reading and validating config")
  class WhenReadingAndValidatingConfig {

    @Test
    @DisplayName("With nominal config file")
    void withNominalConfigFile_shouldSuccess() throws IOException {
      // Given
      String nominalConfigFileName = "whenReadingAndValidatingConfig_withNominalConfigFile.conf";
      Path nominalConfigFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(),
          nominalConfigFileName);

      String configFileName = "test.conf";
      Path configFile = dataFolder.resolve(configFileName);
      Files.copy(nominalConfigFile, configFile);

      // When
      DataSourceProperties dataSourceProperties =
          configManager.readAndValidate(configFileName, DataSourceValidatingProperties.class);

      // Then
      assertThat(dataSourceProperties)
          .isEqualTo(DataSourceProperties.of(DataSourceType.SQLITE, "patch_place_break_tag",
              DbmsServerProperties.of(DbmsHostProperties.of("localhost", 3306, true),
                  CredentialsProperties.of("username", "password"), "database"),
              ConnectionPoolProperties.of(30000, 10)));
    }

    @Test
    @DisplayName("With missing required fields in config file")
    void withMissingRequiredFieldsInConfigFile_shouldThrowException() throws IOException {
      // Given
      String invalidConfigFileName =
          "whenReadingAndValidatingConfig_withMissingRequiredFieldsInConfigFile.conf";
      Path invalidConfigFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(),
          invalidConfigFileName);

      String configFileName = "test.conf";
      Path configFile = dataFolder.resolve(configFileName);
      Files.copy(invalidConfigFile, configFile);

      // When
      ThrowingCallable throwingCallable =
          () -> configManager.readAndValidate(configFileName, DataSourceValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigSerializationException.class);
    }

    @Test
    @DisplayName("With invalid config file")
    void withInvalidConfigFile_shouldThrowException() throws IOException {
      // Given
      String invalidConfigFileName = "whenReadingAndValidatingConfig_withInvalidConfigFile.conf";
      Path invalidConfigFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(),
          invalidConfigFileName);

      String configFileName = "test.conf";
      Path configFile = dataFolder.resolve(configFileName);
      Files.copy(invalidConfigFile, configFile);

      // When
      ThrowingCallable throwingCallable =
          () -> configManager.readAndValidate(configFileName, DataSourceValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(PropertiesValidationException.class);
    }

    @Test
    @DisplayName("With empty config file")
    void withEmptyConfigFile_shouldThrowException() throws IOException {
      // Given
      String emptyConfigFileName = "whenReadingAndValidatingConfig_withEmptyConfigFile.conf";
      Path emptyConfigFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), emptyConfigFileName);

      String configFileName = "test.conf";
      Path configFile = dataFolder.resolve(configFileName);
      Files.copy(emptyConfigFile, configFile);

      // When
      ThrowingCallable throwingCallable =
          () -> configManager.readAndValidate(configFileName, DataSourceValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class);
    }

    @Test
    @DisplayName("Without existing config file")
    void withoutExistingConfigFile_shouldThrowException() {
      // Given
      String configFileName = "test.conf";

      // When
      ThrowingCallable throwingCallable =
          () -> configManager.readAndValidate(configFileName, DataSourceValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ConfigException.class);
    }
  }
}
