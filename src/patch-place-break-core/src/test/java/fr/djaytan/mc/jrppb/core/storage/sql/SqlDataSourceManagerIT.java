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
package fr.djaytan.mc.jrppb.core.storage.sql;

import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_SQLITE_DATA_SOURCE_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.nominalMysqlDataSourceProperties;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_DATABASE_NAME;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerCredentialsProperties;
import fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostProperties;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.JdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.MysqlJdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.SqliteJdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.provider.FlywayProvider;
import fr.djaytan.mc.jrppb.core.storage.sql.provider.HikariDataSourceProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ExtendWith(MockitoExtension.class)
class SqlDataSourceManagerIT {

  private static final String DBMS_HOSTNAME = "localhost";
  private static final int DBMS_ORIGINAL_PORT = 3306;

  @SuppressWarnings("resource") // Reusable containers feature enabled: do not clean-up containers!
  private static final MySQLContainer<?> MYSQL_CONTAINER =
      new MySQLContainer<>("mysql:8.1.0-oracle")
          .withDatabaseName(NOMINAL_DBMS_SERVER_DATABASE_NAME)
          .withReuse(true);

  @SuppressWarnings("resource") // Reusable containers feature enabled: do not clean-up containers!
  private static final MariaDBContainer<?> MARIADB_CONTAINER =
      new MariaDBContainer<>("mariadb:11.1.2-jammy")
          .withDatabaseName(NOMINAL_DBMS_SERVER_DATABASE_NAME)
          .withReuse(true);

  private SqlDataSourceManager sqlDataSourceManager;

  @BeforeAll
  static void beforeAll() {
    Startables.deepStart(MYSQL_CONTAINER, MARIADB_CONTAINER).join();
  }

  @AfterEach
  void tearDown() {
    sqlDataSourceManager.disconnect();
  }

  @ParameterizedTest
  @MethodSource
  void whenConnectingToExistingDatabase_shouldNotThrow(
      @NotNull SqlDataSourceManager sqlDataSourceManager) {
    // Given
    this.sqlDataSourceManager = sqlDataSourceManager;

    // When
    ThrowingCallable throwingCallable = sqlDataSourceManager::connect;

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  private static @NotNull Stream<Arguments> whenConnectingToExistingDatabase_shouldNotThrow()
      throws IOException {
    return Stream.of(forSqlite(), forMysql(), forMariadb());
  }

  private static @NotNull Arguments forSqlite() throws IOException {
    Path sqliteDatabaseFile = Files.createTempFile("ppb-datasourcemanager", "");
    JdbcUrl jdbcUrl = new SqliteJdbcUrl(sqliteDatabaseFile);

    HikariDataSourceProvider hikariDataSourceProvider =
        new HikariDataSourceProvider(NOMINAL_SQLITE_DATA_SOURCE_PROPERTIES, jdbcUrl);
    HikariDataSource hikariDataSource = hikariDataSourceProvider.get();
    DatabaseMediator databaseMediator = new DatabaseMediator(hikariDataSource);

    ClassLoader classLoader = SqlDataSourceManager.class.getClassLoader();
    FlywayProvider flywayProvider =
        new FlywayProvider(classLoader, hikariDataSource, NOMINAL_SQLITE_DATA_SOURCE_PROPERTIES);
    Flyway flyway = flywayProvider.get();
    DataMigrationExecutor dataMigrationExecutor = new DataMigrationExecutor(flyway);

    return arguments(
        named("SQLite", new SqlDataSourceManager(databaseMediator, dataMigrationExecutor)));
  }

  private static @NotNull Arguments forMysql() {
    int dbmsPort = MYSQL_CONTAINER.getMappedPort(DBMS_ORIGINAL_PORT);
    String username = MYSQL_CONTAINER.getUsername();
    String password = MYSQL_CONTAINER.getUsername();
    return arguments(named("MySQL", createDbmsSqlDataSourceManager(dbmsPort, username, password)));
  }

  private static @NotNull Arguments forMariadb() {
    int dbmsPort = MARIADB_CONTAINER.getMappedPort(DBMS_ORIGINAL_PORT);
    String username = MARIADB_CONTAINER.getUsername();
    String password = MARIADB_CONTAINER.getUsername();
    return arguments(
        named("MariaDB", createDbmsSqlDataSourceManager(dbmsPort, username, password)));
  }

  private static @NotNull SqlDataSourceManager createDbmsSqlDataSourceManager(
      int dbmsPort, @NotNull String username, @NotNull String password) {
    var dataSourceProperties =
        nominalMysqlDataSourceProperties(
            new DbmsServerHostProperties(DBMS_HOSTNAME, dbmsPort, true),
            new DbmsServerCredentialsProperties(username, password));

    JdbcUrl jdbcUrl = new MysqlJdbcUrl(dataSourceProperties);
    HikariDataSourceProvider hikariDataSourceProvider =
        new HikariDataSourceProvider(dataSourceProperties, jdbcUrl);
    HikariDataSource hikariDataSource = hikariDataSourceProvider.get();
    DatabaseMediator databaseMediator = new DatabaseMediator(hikariDataSource);

    ClassLoader classLoader = SqlDataSourceManager.class.getClassLoader();
    FlywayProvider flywayProvider =
        new FlywayProvider(classLoader, hikariDataSource, dataSourceProperties);
    Flyway flyway = flywayProvider.get();
    DataMigrationExecutor dataMigrationExecutor = new DataMigrationExecutor(flyway);

    return new SqlDataSourceManager(databaseMediator, dataMigrationExecutor);
  }
}
