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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.JdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.SqliteJdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.provider.HikariDataSourceProvider;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseMediatorTest {

  @TempDir private Path dataFolder;
  @AutoClose private DatabaseMediator databaseMediator;

  @BeforeEach
  void setUp() {
    Path sqliteDatabaseFile = dataFolder.resolve("sqlite-data.db");
    JdbcUrl jdbcUrl = new SqliteJdbcUrl(sqliteDatabaseFile);
    HikariDataSourceProvider hikariDataSourceProvider =
        new HikariDataSourceProvider(NOMINAL_SQLITE_DATA_SOURCE_PROPERTIES, jdbcUrl);
    HikariDataSource hikariDataSource = hikariDataSourceProvider.get();

    databaseMediator = new DatabaseMediator(hikariDataSource);
  }

  @Test
  void whenUsingConnectionForExecutingRequest_shouldExecuteCallbackSuccessfully() {
    // Given
    String testSystemPropertyName = "test-connection-pool";
    Consumer<Connection> callback =
        connection -> System.setProperty(testSystemPropertyName, "Lambda executed successfully");

    // When
    databaseMediator.dispatchRequest(callback);

    // Then
    assertThat(System.getProperty(testSystemPropertyName))
        .isEqualTo("Lambda executed successfully");
  }

  @Test
  void whenUsingConnectionForExecutingErrorProneRequest_shouldRethrowInitialException() {
    // Given
    Consumer<Connection> callback =
        connection -> {
          throw new IllegalStateException("test");
        };

    // When
    Exception exception = catchException(() -> databaseMediator.dispatchRequest(callback));

    // Then
    assertThat(exception).isExactlyInstanceOf(IllegalStateException.class).hasMessage("test");
  }

  @Test
  void whenUsingConnectionForExecutingQuery_shouldExecuteCallbackSuccessfully() {
    // Given
    Function<Connection, Optional<Boolean>> callback = connection -> Optional.of(true);

    // When
    Optional<Boolean> retrievedValue = databaseMediator.dispatchQuery(callback);

    // Then
    assertThat(retrievedValue).contains(true);
  }

  @Test
  void whenUsingConnectionForExecutingErrorProneQuery_shouldRethrowInitialException() {
    // Given
    Function<Connection, Optional<Boolean>> callback =
        connection -> {
          throw new IllegalStateException("test");
        };

    // When
    Exception exception = catchException(() -> databaseMediator.dispatchQuery(callback));

    // Then
    assertThat(exception).isExactlyInstanceOf(IllegalStateException.class).hasMessage("test");
  }
}
