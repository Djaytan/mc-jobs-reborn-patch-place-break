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
package fr.djaytan.mc.jrppb.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import fr.djaytan.mc.jrppb.api.PatchPlaceBreakApi;
import fr.djaytan.mc.jrppb.commons.test.TestResourcesHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

class PatchPlaceBreakCoreIT {

  private static final int DATABASE_ORIGINAL_PORT = 3306;
  private static final String DATABASE_NAME = "patch_place_break";

  @SuppressWarnings("resource") // Reusable containers feature enabled: do not clean-up containers!
  private static final MySQLContainer<?> MYSQL_CONTAINER =
      new MySQLContainer<>("mysql:8.1.0-oracle").withDatabaseName(DATABASE_NAME).withReuse(true);

  @SuppressWarnings("resource") // Reusable containers feature enabled: do not clean-up containers!
  private static final MariaDBContainer<?> MARIADB_CONTAINER =
      new MariaDBContainer<>("mariadb:11.1.2-jammy")
          .withDatabaseName(DATABASE_NAME)
          .withReuse(true);

  private static final String CONFIG_DATA_SOURCE_FILE_NAME = "dataSource.conf";
  private static final String RESTRICTED_BLOCKS_CONFIG_FILE_NAME = "restrictedBlocks.conf";
  private static final String SQLITE_DATABASE_FILE_NAME = "sqlite-data.db";

  @TempDir private Path dataFolder;
  private final PatchPlaceBreakCore patchPlaceBreakCore = new PatchPlaceBreakCore();

  @BeforeAll
  static void beforeAll() {
    Startables.deepStart(MYSQL_CONTAINER, MARIADB_CONTAINER).join();
  }

  @AfterEach
  void afterEach() {
    patchPlaceBreakCore.disable();
  }

  @Nested
  class WhenEnabling {

    @Test
    void withDefaultSqliteSetup_shouldDefaultFilesCreatedAsExpected() {
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
      Path actualRestrictedBlocksConfigFile =
          dataFolder.resolve(RESTRICTED_BLOCKS_CONFIG_FILE_NAME);

      Path sqliteDatabaseFile = dataFolder.resolve(SQLITE_DATABASE_FILE_NAME);

      assertAll(
          () -> assertThat(patchPlaceBreakApi).isNotNull(),
          () ->
              assertThat(actualConfDataSourceFile)
                  .hasSameTextualContentAs(expectedConfDataSourceFile),
          () ->
              assertThat(actualRestrictedBlocksConfigFile)
                  .hasSameTextualContentAs(expectedRestrictedBlocksConfigFile),
          () -> assertThat(sqliteDatabaseFile).isNotEmptyFile());
    }

    @ParameterizedTest
    @MethodSource
    void withCustomSetup(@NotNull JdbcDatabaseContainer<?> jdbcContainer) throws IOException {
      // Given
      int dbmsPort = jdbcContainer.getMappedPort(DATABASE_ORIGINAL_PORT);
      String username = jdbcContainer.getUsername();
      String password = jdbcContainer.getPassword();
      String givenConfigFileContent =
          String.format(
              TestResourcesHelper.getClassResourceAsString(
                  this.getClass(),
                  "whenEnabling_withCustomSetup_givenDataSourceConfigFileTemplate.conf",
                  false),
              dbmsPort,
              username,
              password);
      Path configFile = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);
      Files.writeString(configFile, givenConfigFileContent);

      ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();

      // When
      PatchPlaceBreakApi patchPlaceBreakApi =
          patchPlaceBreakCore.enable(classLoader, Clock.systemUTC(), dataFolder);

      // Then
      Path actualConfDataSourceFile = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);

      assertAll(
          () -> assertThat(patchPlaceBreakApi).isNotNull(),
          () -> assertThat(actualConfDataSourceFile).exists().hasContent(givenConfigFileContent));
    }

    private static @NotNull Stream<Arguments> withCustomSetup() {
      return Stream.of(
          arguments(named("MySQL", MYSQL_CONTAINER)),
          arguments(named("MariaDB", MARIADB_CONTAINER)));
    }
  }
}
