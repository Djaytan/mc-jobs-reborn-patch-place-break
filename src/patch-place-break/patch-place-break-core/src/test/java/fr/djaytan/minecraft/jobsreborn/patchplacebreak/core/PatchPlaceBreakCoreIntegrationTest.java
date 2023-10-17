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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(SoftAssertionsExtension.class)
class PatchPlaceBreakCoreIntegrationTest {

  private static final String CONFIG_DATA_SOURCE_FILE_NAME = "dataSource.conf";
  private static final String RESTRICTED_BLOCKS_CONFIG_FILE_NAME = "restrictedBlocks.conf";
  private static final String SQLITE_DATABASE_FILE_NAME = "sqlite-data.db";

  @InjectSoftAssertions private SoftAssertions softly;
  @TempDir private Path dataFolder;
  private final PatchPlaceBreakCore patchPlaceBreakCore = new PatchPlaceBreakCore();

  @AfterEach
  @SneakyThrows
  void afterEach() {
    patchPlaceBreakCore.disable();
  }

  @Test
  @DisplayName("When enabling with default setup (SQLite)")
  @SneakyThrows
  void whenEnabling_withDefaultSetup() {
    // Given
    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();

    // When
    PatchPlaceBreakApi patchPlaceBreakApi =
        patchPlaceBreakCore.enable(classLoader, Clock.systemUTC(), dataFolder);

    // Then
    Path expectedConfDataSourceFile =
        TestResourcesHelper.getClassResourceAsAbsolutePath(
            this.getClass(), "whenEnabling_withDefaultSetup_expectedDataSourceConfigFile.conf");
    Path actualConfDataSourceFile = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);

    Path expectedRestrictedBlocksConfigFile =
        TestResourcesHelper.getClassResourceAsAbsolutePath(
            this.getClass(),
            "whenEnabling_withDefaultSetup_expectedRestrictedBlocksConfigFile.conf");
    Path actualRestrictedBlocksConfigFile = dataFolder.resolve(RESTRICTED_BLOCKS_CONFIG_FILE_NAME);

    Path sqliteDatabaseFile = dataFolder.resolve(SQLITE_DATABASE_FILE_NAME);

    softly.assertThat(patchPlaceBreakApi).isNotNull();
    softly.assertThat(actualConfDataSourceFile).hasSameTextualContentAs(expectedConfDataSourceFile);
    softly
        .assertThat(actualRestrictedBlocksConfigFile)
        .hasSameTextualContentAs(expectedRestrictedBlocksConfigFile);
    softly.assertThat(sqliteDatabaseFile).isNotEmptyFile();
  }

  @ParameterizedTest
  @ValueSource(strings = {"mysql", "mariadb"})
  @DisplayName("When enabling with custom setup")
  @SneakyThrows
  void whenEnabling_withCustomSetup(@NotNull String dbmsType) {
    // Given
    int dbmsPort = Integer.parseInt(System.getProperty(String.format("%s.port", dbmsType)));
    String givenConfigFileContent =
        String.format(
            TestResourcesHelper.getClassResourceAsString(
                this.getClass(),
                "whenEnabling_withCustomSetup_givenDataSourceConfigFileTemplate.conf",
                false),
            dbmsPort);
    Path configFile = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);
    Files.writeString(configFile, givenConfigFileContent);

    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();

    // When
    PatchPlaceBreakApi patchPlaceBreakApi =
        patchPlaceBreakCore.enable(classLoader, Clock.systemUTC(), dataFolder);

    // Then
    Path actualConfDataSourceFile = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);

    softly.assertThat(patchPlaceBreakApi).isNotNull();
    softly.assertThat(actualConfDataSourceFile).exists().hasContent(givenConfigFileContent);
  }
}
