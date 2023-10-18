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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.sqlite.SqliteJdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.provider.HikariDataSourceProvider;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConnectionPoolTest {

  @TempDir private Path dataFolder;
  private ConnectionPool connectionPool;

  @BeforeEach
  void beforeEach() {
    Path sqliteDatabaseFile = dataFolder.resolve("sqlite-data.db");
    JdbcUrl jdbcUrl = new SqliteJdbcUrl(sqliteDatabaseFile);
    DataSourceProperties dataSourceProperties = DataSourcePropertiesMock.get(DataSourceType.SQLITE);

    HikariDataSourceProvider hikariDataSourceProvider =
        new HikariDataSourceProvider(dataSourceProperties, jdbcUrl);
    HikariDataSource hikariDataSource = hikariDataSourceProvider.get();

    connectionPool = new ConnectionPool(hikariDataSource);
  }

  @AfterEach
  void afterEach() {
    connectionPool.close();
  }

  @Test
  @DisplayName("When using connection for executing request successfully")
  void whenUsingConnectionForExecutingRequestSuccessfully() {
    // Given
    String testSystemPropertyName = "test-connection-pool";
    Consumer<Connection> callback =
        connection -> System.setProperty(testSystemPropertyName, "Lambda executed successfully");

    // When
    connectionPool.useConnection(callback);

    // Then
    assertThat(System.getProperty(testSystemPropertyName))
        .isEqualTo("Lambda executed successfully");
  }

  @Test
  @DisplayName("When using connection for executing request with exception thrown")
  void whenUsingConnectionForExecutingRequestWithExceptionThrown() {
    // Given
    Consumer<Connection> callback =
        connection -> {
          throw new IllegalStateException("test");
        };

    // When
    Exception exception = catchException(() -> connectionPool.useConnection(callback));

    // Then
    assertThat(exception).isExactlyInstanceOf(IllegalStateException.class).hasMessage("test");
  }

  @Test
  @DisplayName("When using connection for executing query successfully")
  void whenUsingConnectionForExecutingQuerySuccessfully() {
    // Given
    Function<Connection, Optional<Boolean>> callback = connection -> Optional.of(true);

    // When
    Optional<Boolean> retrievedValue = connectionPool.useConnection(callback);

    // Then
    assertThat(retrievedValue).contains(true);
  }

  @Test
  @DisplayName("When using connection for executing query with exception thrown")
  void whenUsingConnectionForExecutingQueryWithExceptionThrown() {
    // Given
    Function<Connection, Optional<Boolean>> callback =
        connection -> {
          throw new IllegalStateException("test");
        };

    // When
    Exception exception = catchException(() -> connectionPool.useConnection(callback));

    // Then
    assertThat(exception).isExactlyInstanceOf(IllegalStateException.class).hasMessage("test");
  }
}
