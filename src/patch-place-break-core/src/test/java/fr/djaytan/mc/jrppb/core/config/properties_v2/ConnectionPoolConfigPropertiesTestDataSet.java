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
package fr.djaytan.mc.jrppb.core.config.properties_v2;

import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_TIMEOUT;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_POOL_SIZE;

public final class ConnectionPoolConfigPropertiesTestDataSet {

  public static final ConnectionPoolConfigProperties NOMINAL_CONNECTION_POOL_CONFIG_PROPERTIES =
      new ConnectionPoolConfigProperties(NOMINAL_CONNECTION_TIMEOUT, NOMINAL_POOL_SIZE);

  public static final String NOMINAL_SERIALIZED_CONNECTION_POOL_CONFIG_PROPERTIES =
      """
      # The connection timeout (in milliseconds)
      # Corresponds to the maximum time the connection pool will wait to acquire a new connection
      # from the DBMS server
      # Not applicable for SQLite
      # Accepted range values: [1-600000]
      connectionTimeout=60000
      # The number of DBMS connections in the pool
      # Could be best determined by the executing environment
      # Accepted range values: [1-100]
      poolSize=20
      """;
}
