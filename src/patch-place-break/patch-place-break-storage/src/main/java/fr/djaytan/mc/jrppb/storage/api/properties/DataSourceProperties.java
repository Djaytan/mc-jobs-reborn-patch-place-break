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
package fr.djaytan.mc.jrppb.storage.api.properties;

import org.jetbrains.annotations.NotNull;

/** Represents the properties related to the data source to be used for storage. */
public interface DataSourceProperties {

  /**
   * Gets the data source type to be used for storage.
   *
   * @return The data source type to be used for storage.
   */
  @NotNull
  DataSourceType getType();

  /**
   * Gets the table name to be used for storage.
   *
   * @return The table name to be used for storage.
   */
  @NotNull
  String getTable();

  /**
   * Gets the properties related to the DBMS server (not applicable for all data source types like
   * {@link DataSourceType#SQLITE}).
   *
   * @return The properties related to the DBMS server.
   */
  @NotNull
  DbmsServerProperties getDbmsServer();

  /**
   * Gets the properties related to the connection pool.
   *
   * @return The properties related to the connection pool.
   */
  @NotNull
  ConnectionPoolProperties getConnectionPool();
}
