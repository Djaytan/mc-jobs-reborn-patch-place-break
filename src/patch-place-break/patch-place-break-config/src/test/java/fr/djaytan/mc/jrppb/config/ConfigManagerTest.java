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
package fr.djaytan.mc.jrppb.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import fr.djaytan.mc.jrppb.commons.test.TestResourcesHelper;
import fr.djaytan.mc.jrppb.config.properties.ConnectionPoolPropertiesImpl;
import fr.djaytan.mc.jrppb.config.properties.DataSourcePropertiesImpl;
import fr.djaytan.mc.jrppb.config.properties.DbmsCredentialsPropertiesImpl;
import fr.djaytan.mc.jrppb.config.properties.DbmsHostPropertiesImpl;
import fr.djaytan.mc.jrppb.config.properties.DbmsServerPropertiesImpl;
import fr.djaytan.mc.jrppb.config.properties.Properties;
import fr.djaytan.mc.jrppb.config.serialization.ConfigSerializationException;
import fr.djaytan.mc.jrppb.config.serialization.ConfigSerializer;
import fr.djaytan.mc.jrppb.config.testutils.ValidatorTestWrapper;
import fr.djaytan.mc.jrppb.config.validation.PropertiesValidationException;
import fr.djaytan.mc.jrppb.config.validation.PropertiesValidator;
import fr.djaytan.mc.jrppb.storage.api.properties.DataSourceProperties;
import fr.djaytan.mc.jrppb.storage.api.properties.DataSourceType;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConfigManagerTest {

  private ConfigManager configManager;
  @Spy private ConfigSerializer configSerializerSpied;
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
  void afterEach() throws IOException {
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
      DataSourcePropertiesImpl defaultProperties = new DataSourcePropertiesImpl();
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
    void withGenericDefaultProperties_shouldSerializeConfig() throws IOException {
      // Given
      String configFileName = "nominalCase.conf";
      Properties defaultProperties = new DataSourcePropertiesImpl();

      // When
      configManager.createDefaultIfNotExists(configFileName, defaultProperties);

      // Then
      String expectedConfigContent =
          TestResourcesHelper.getClassResourceAsString(
              this.getClass(), "whenCreatingConfig_withGenericDefaultProperties.conf", false);
      Path configFile = dataFolder.resolve(configFileName);
      String actualConfigContent = new String(Files.readAllBytes(configFile));

      assertThat(actualConfigContent).containsIgnoringNewLines(expectedConfigContent);
    }

    @Test
    @DisplayName("With exception thrown at serialization time")
    void withExceptionThrownAtSerializationTime_shouldThrowWrapperException() {
      // Given
      String configFileName = "exception.conf";
      DataSourcePropertiesImpl defaultProperties = new DataSourcePropertiesImpl();
      doThrow(ConfigSerializationException.class)
          .when(configSerializerSpied)
          .serialize(any(), any());

      // When
      Exception exception =
          catchException(
              () -> configManager.createDefaultIfNotExists(configFileName, defaultProperties));

      // Then
      assertThat(exception).isExactlyInstanceOf(ConfigSerializationException.class);
    }

    @Test
    @DisplayName("With invalid default properties")
    void withInvalidDefaultProperties_shouldThrowException() {
      // Given
      String configFileName = "exception.conf";
      DataSourcePropertiesImpl defaultProperties =
          new DataSourcePropertiesImpl(null, null, null, null);

      // When
      Exception exception =
          catchException(
              () -> configManager.createDefaultIfNotExists(configFileName, defaultProperties));

      // Then
      assertThat(exception).isExactlyInstanceOf(PropertiesValidationException.class);
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
      Path nominalConfigFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(
              this.getClass(), nominalConfigFileName);

      String configFileName = "test.conf";
      Path configFile = dataFolder.resolve(configFileName);
      Files.copy(nominalConfigFile, configFile);

      // When
      DataSourceProperties dataSourceProperties =
          configManager.readAndValidate(configFileName, DataSourcePropertiesImpl.class);

      // Then
      assertThat(dataSourceProperties)
          .isEqualTo(
              new DataSourcePropertiesImpl(
                  DataSourceType.SQLITE,
                  "patch_place_break_tag",
                  new DbmsServerPropertiesImpl(
                      new DbmsHostPropertiesImpl("localhost", 3306, true),
                      new DbmsCredentialsPropertiesImpl("username", "password"),
                      "database"),
                  new ConnectionPoolPropertiesImpl(30000, 10)));
    }

    @Test
    @DisplayName("With missing required fields in config file")
    void withMissingRequiredFieldsInConfigFile_shouldThrowException() throws IOException {
      // Given
      String invalidConfigFileName =
          "whenReadingAndValidatingConfig_withMissingRequiredFieldsInConfigFile.conf";
      Path invalidConfigFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(
              this.getClass(), invalidConfigFileName);

      String configFileName = "test.conf";
      Path configFile = dataFolder.resolve(configFileName);
      Files.copy(invalidConfigFile, configFile);

      // When
      Exception exception =
          catchException(
              () -> configManager.readAndValidate(configFileName, DataSourcePropertiesImpl.class));

      // Then
      assertThat(exception).isExactlyInstanceOf(ConfigSerializationException.class);
    }

    @Test
    @DisplayName("With invalid config file")
    void withInvalidConfigFile_shouldThrowException() throws IOException {
      // Given
      String invalidConfigFileName = "whenReadingAndValidatingConfig_withInvalidConfigFile.conf";
      Path invalidConfigFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(
              this.getClass(), invalidConfigFileName);

      String configFileName = "test.conf";
      Path configFile = dataFolder.resolve(configFileName);
      Files.copy(invalidConfigFile, configFile);

      // When
      Exception exception =
          catchException(
              () -> configManager.readAndValidate(configFileName, DataSourcePropertiesImpl.class));

      // Then
      assertThat(exception).isExactlyInstanceOf(PropertiesValidationException.class);
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
      Exception exception =
          catchException(
              () -> configManager.readAndValidate(configFileName, DataSourcePropertiesImpl.class));

      // Then
      assertThat(exception).isExactlyInstanceOf(ConfigException.class);
    }

    @Test
    @DisplayName("Without existing config file")
    void withoutExistingConfigFile_shouldThrowException() {
      // Given
      String configFileName = "test.conf";

      // When
      Exception exception =
          catchException(
              () -> configManager.readAndValidate(configFileName, DataSourcePropertiesImpl.class));

      // Then
      assertThat(exception).isExactlyInstanceOf(ConfigException.class);
    }
  }
}
