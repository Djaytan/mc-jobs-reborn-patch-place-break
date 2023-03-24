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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;

import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.mysql.MysqlJdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.sqlite.SqliteJdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.provider.FlywayProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.provider.HikariDataSourceProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SqlDataSourceManagerIntegrationTest {

  private SqlDataSourceManager sqlDataSourceManager;

  @AfterEach
  void afterEach() {
    sqlDataSourceManager.disconnect();
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("When connecting to existing database")
  @SneakyThrows
  void whenConnectingToExistingDatabase_shouldNotThrow(
      @NotNull SqlDataSourceManager sqlDataSourceManager) {
    // Given
    this.sqlDataSourceManager = sqlDataSourceManager;

    // When
    ThrowingCallable throwingCallable = sqlDataSourceManager::connect;

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  private static @NotNull Stream<Arguments> whenConnectingToExistingDatabase_shouldNotThrow() {
    return Stream.of(forSqlite(), forMysql(), forMariadb());
  }

  @SneakyThrows
  private static @NotNull Arguments forSqlite() {
    DataSourceProperties dataSourcePropertiesMocked =
        DataSourcePropertiesMock.get(DataSourceType.SQLITE);
    Path sqliteDatabaseFile = Files.createTempFile("ppb-datasourcemanager", "");
    JdbcUrl jdbcUrl = new SqliteJdbcUrl(sqliteDatabaseFile);

    HikariDataSourceProvider hikariDataSourceProvider =
        new HikariDataSourceProvider(dataSourcePropertiesMocked, jdbcUrl);
    HikariDataSource hikariDataSource = hikariDataSourceProvider.get();
    ConnectionPool connectionPool = new ConnectionPool(hikariDataSource);

    ClassLoader classLoader = SqlDataSourceManager.class.getClassLoader();
    FlywayProvider flywayProvider =
        new FlywayProvider(classLoader, hikariDataSource, dataSourcePropertiesMocked);
    Flyway flyway = flywayProvider.get();
    DataMigrationExecutor dataMigrationExecutor = new DataMigrationExecutor(flyway);

    return arguments(
        named("For SQLite", new SqlDataSourceManager(connectionPool, dataMigrationExecutor)));
  }

  private static @NotNull Arguments forMysql() {
    int dbmsPort = Integer.parseInt(System.getProperty("mysql.port"));
    return arguments(named("For MySQL", createDbmsSqlDataSourceManager(dbmsPort)));
  }

  private static @NotNull Arguments forMariadb() {
    int dbmsPort = Integer.parseInt(System.getProperty("mariadb.port"));
    return arguments(named("For MariaDB", createDbmsSqlDataSourceManager(dbmsPort)));
  }

  private static @NotNull SqlDataSourceManager createDbmsSqlDataSourceManager(int dbmsPort) {
    DataSourceProperties dataSourcePropertiesMocked =
        DataSourcePropertiesMock.get(DataSourceType.MYSQL);
    DbmsHostProperties dbmsHostProperties = dataSourcePropertiesMocked.getDbmsServer().getHost();
    given(dbmsHostProperties.getPort()).willReturn(dbmsPort);

    JdbcUrl jdbcUrl = new MysqlJdbcUrl(dataSourcePropertiesMocked);
    HikariDataSourceProvider hikariDataSourceProvider =
        new HikariDataSourceProvider(dataSourcePropertiesMocked, jdbcUrl);
    HikariDataSource hikariDataSource = hikariDataSourceProvider.get();
    ConnectionPool connectionPool = new ConnectionPool(hikariDataSource);

    ClassLoader classLoader = SqlDataSourceManager.class.getClassLoader();
    FlywayProvider flywayProvider =
        new FlywayProvider(classLoader, hikariDataSource, dataSourcePropertiesMocked);
    Flyway flyway = flywayProvider.get();
    DataMigrationExecutor dataMigrationExecutor = new DataMigrationExecutor(flyway);

    return new SqlDataSourceManager(connectionPool, dataMigrationExecutor);
  }
}
