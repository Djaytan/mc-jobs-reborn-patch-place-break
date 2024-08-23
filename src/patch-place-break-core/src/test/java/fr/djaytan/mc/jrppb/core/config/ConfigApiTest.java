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
package fr.djaytan.mc.jrppb.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import fr.djaytan.mc.jrppb.commons.test.TestResourcesHelper;
import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializer;
import fr.djaytan.mc.jrppb.core.config.testutils.ValidatorTestWrapper;
import fr.djaytan.mc.jrppb.core.config.validation.PropertiesValidator;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigApiTest {

  @AutoClose private final FileSystem imfs = Jimfs.newFileSystem(Configuration.unix());

  private ConfigApi configApi;
  private Path dataFolder;

  @BeforeEach
  void setUp() {
    dataFolder = imfs.getPath("");
    ConfigSerializer configSerializer = new ConfigSerializer();
    PropertiesValidator propertiesValidator =
        new PropertiesValidator(ValidatorTestWrapper.getValidator());
    ConfigManager configManager =
        new ConfigManager(dataFolder, configSerializer, propertiesValidator);
    configApi = new ConfigApi(configManager);
  }

  @Test
  void whenRetrievingDataSourceProperties_shouldSuccess() throws IOException {
    // Given

    // When
    configApi.getDataSourceProperties();

    // Then
    String expectedContent =
        TestResourcesHelper.getClassResourceAsString(
            this.getClass(), "whenRetrievingDataSourceProperties.conf", false);
    String actualContent = new String(Files.readAllBytes(dataFolder.resolve("dataSource.conf")));
    assertThat(actualContent).containsIgnoringNewLines(expectedContent);
  }
}
