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

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the connection pool used by the program to manage all connections with DBMS server.
 *
 * <p>To use it, {@link #useConnection(Consumer)} and {@link #useConnection(Function)} methods can
 * be used to execute requests and queries.
 *
 * <p>When stopping the program, the method {@link #close()} must be called to cleanly stop the
 * connection pool by releasing all remaining opened connections and related resources.
 */
@Slf4j
@Singleton
public class ConnectionPool implements AutoCloseable {

  private final HikariDataSource hikariDataSource;

  @Inject
  public ConnectionPool(@NotNull HikariDataSource hikariDataSource) {
    this.hikariDataSource = hikariDataSource;
  }

  /**
   * Uses connection for executing request.
   *
   * @param consumer The callback to execute request.
   */
  public void useConnection(@NotNull Consumer<Connection> consumer) {
    try (Connection connection = hikariDataSource.getConnection()) {
      consumer.accept(connection);
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionLifecycleManagement(e);
    }
  }

  /**
   * Uses connection for executing query.
   *
   * @param function The callback to execute query.
   * @return The value obtained after executing the query.
   * @param <T> The type of the value to be retrieved.
   */
  public <T> @NotNull Optional<T> useConnection(
      @NotNull Function<Connection, Optional<T>> function) {
    try (Connection connection = hikariDataSource.getConnection()) {
      return function.apply(connection);
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionLifecycleManagement(e);
    }
  }

  /**
   * Disables the connection pool by releasing all the currently opened connections and related
   * resources.
   */
  @Override
  public void close() {
    hikariDataSource.close();
    log.atInfo().log("Disconnected from the database '{}'", hikariDataSource.getJdbcUrl());
  }
}
