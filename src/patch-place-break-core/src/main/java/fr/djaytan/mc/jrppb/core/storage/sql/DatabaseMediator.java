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

import com.zaxxer.hikari.HikariDataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the connection pool used by the program to manage all connections with DBMS server.
 *
 * <p>To use it, {@link #dispatchRequest(Consumer)} and {@link #dispatchQuery(Function)} methods can
 * be used to execute requests and queries.
 *
 * <p>When stopping the program, the method {@link #close()} must be called to cleanly stop the
 * connection pool by releasing all remaining opened connections and related resources.
 */
@Singleton
public class DatabaseMediator implements AutoCloseable {

  private static final Logger LOG = LoggerFactory.getLogger(DatabaseMediator.class);

  private final HikariDataSource hikariDataSource;

  @Inject
  public DatabaseMediator(@NotNull HikariDataSource hikariDataSource) {
    this.hikariDataSource = hikariDataSource;
  }

  /**
   * Uses connection for dispatching a request.
   *
   * @param consumer The callback dispatching a request based on a provided connection.
   */
  public void dispatchRequest(@NotNull Consumer<Connection> consumer) {
    try (Connection connection = hikariDataSource.getConnection()) {
      consumer.accept(connection);
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionLifecycleManagement(e);
    }
  }

  /**
   * Uses connection for dispatching a query.
   *
   * @param function The callback dispatching a query based on a provided connection.
   * @return The value obtained after executing the query.
   * @param <T> The type of the value to be retrieved.
   */
  public <T> @NotNull Optional<T> dispatchQuery(
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
    LOG.atInfo().log("Disconnected from the database '{}'", hikariDataSource.getJdbcUrl());
  }
}
