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
package fr.djaytan.mc.jrppb.core.config.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import fr.djaytan.mc.jrppb.commons.test.TestResourcesHelper;
import fr.djaytan.mc.jrppb.core.config.properties.DbmsHostPropertiesImpl;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.serialize.SerializationException;

class ConfigSerializerTest {

  private ConfigSerializer configSerializer = new ConfigSerializer();
  private FileSystem imfs;

  @BeforeEach
  void beforeEach() {
    configSerializer = new ConfigSerializer();
    imfs = Jimfs.newFileSystem(Configuration.unix());
  }

  @AfterEach
  void afterEach() throws IOException {
    imfs.close();
  }

  @Nested
  class WhenSerializing {

    @Test
    void withNominalValues_shouldCreateAndFillYamlFile() throws IOException {
      // Given
      Path targetFileLocation = imfs.getPath("test.conf");
      DbmsHostPropertiesImpl dbmsHostPropertiesImpl =
          new DbmsHostPropertiesImpl("example.com", 1234, true);

      // When
      configSerializer.serialize(targetFileLocation, dbmsHostPropertiesImpl);

      // Then
      String expectedYamlFile = "whenSerializing_withNominalValues.conf";
      String expectedYaml =
          TestResourcesHelper.getClassResourceAsString(this.getClass(), expectedYamlFile, false);
      String actualYaml = new String(Files.readAllBytes(targetFileLocation));
      assertThat(actualYaml).isEqualToIgnoringNewLines(expectedYaml);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void withCamelCaseFields_shouldSuccess() {
      // Given
      String confFileName = "whenDeserializing_withCamelCaseFields.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsHostPropertiesImpl> properties =
          configSerializer.deserialize(confFile, DbmsHostPropertiesImpl.class);

      // Then
      assertThat(properties).hasValue(new DbmsHostPropertiesImpl("example.com", 1234, true));
    }

    @Test
    void withKebabCaseFields_shouldThrowException() {
      // Given
      String confFileName = "whenDeserializing_withKebabCaseFields.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Exception exception =
          catchException(
              () -> configSerializer.deserialize(confFile, DbmsHostPropertiesImpl.class));

      // Then
      assertThat(exception)
          .isExactlyInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }
  }
}
