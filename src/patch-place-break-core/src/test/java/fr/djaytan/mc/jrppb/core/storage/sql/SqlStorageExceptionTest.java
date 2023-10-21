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

import static org.assertj.core.api.Assertions.assertThat;

import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceType;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class SqlStorageExceptionTest {

  @Test
  void whenInstantiatingConnectionPoolNotSetupException() {
    assertThat(SqlStorageException.connectionPoolNotSetup())
        .hasMessage("The connection pool must be setup before using it");
  }

  @Test
  void whenInstantiatingDatabaseConnectionLifecycleManagementException() {
    // Given
    Throwable cause = new IOException();

    // When
    SqlStorageException sqlStorageException =
        SqlStorageException.databaseConnectionLifecycleManagement(cause);

    // Then
    assertThat(sqlStorageException)
        .hasCause(cause)
        .hasMessage(
            "Something went wrong when managing database connection lifecycle "
                + "(establishment, releasing, ...)");
  }

  @Test
  void whenInstantiatingDatabaseCreationException() {
    // Given
    String databaseName = "patch_place_break";
    Throwable cause = new IOException();

    // When
    SqlStorageException sqlStorageException =
        SqlStorageException.databaseCreation(databaseName, cause);

    // Then
    assertThat(sqlStorageException)
        .hasCause(cause)
        .hasMessage("Unable to create the database 'patch_place_break'");
  }

  @Test
  void whenInstantiatingUnsupportedDataSourceTypeException() {
    assertThat(SqlStorageException.unsupportedDataSourceType(DataSourceType.MYSQL))
        .hasMessage(
            "java.lang.UnsupportedOperationException: Unsupported data source type 'MYSQL'");
  }
}
