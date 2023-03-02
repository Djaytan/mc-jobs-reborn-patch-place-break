/*-
 * #%L
 * JobsReborn-PatchPlaceBreak
 * %%
 * Copyright (C) 2022 - 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 * %%
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
 * #L%
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidator;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigApiTest {

  private ConfigApi configApi;
  private FileSystem imfs;
  private Path dataFolder;

  @BeforeEach
  void beforeEach() {
    imfs = Jimfs.newFileSystem(Configuration.unix());
    dataFolder = imfs.getPath("");
    ConfigSerializer configSerializer = new ConfigSerializer();
    PropertiesValidator propertiesValidator =
        new PropertiesValidator(ValidatorTestWrapper.getValidator());
    ConfigManager configManager =
        new ConfigManager(dataFolder, configSerializer, propertiesValidator);
    configApi = new ConfigApi(configManager);
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    imfs.close();
  }

  @Test
  @DisplayName("When retrieving data source properties")
  @SneakyThrows
  void whenRetrievingDataSourceProperties_shouldSuccess() {
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
