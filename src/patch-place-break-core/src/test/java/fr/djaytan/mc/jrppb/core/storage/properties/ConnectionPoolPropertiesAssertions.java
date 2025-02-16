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
package fr.djaytan.mc.jrppb.core.storage.properties;

import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_TIMEOUT;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_POOL_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class ConnectionPoolPropertiesAssertions {

  public static void assertSuccessfulInstantiation(int connectionPool, int poolSize) {
    assertThat(new ConnectionPoolProperties(connectionPool, poolSize))
        .satisfies(v -> assertThat(v.connectionTimeout()).isEqualTo(connectionPool))
        .satisfies(v -> assertThat(v.poolSize()).isEqualTo(poolSize));
  }

  public static void assertSuccessfulInstantiationWithConnectionPool(int connectionPool) {
    assertSuccessfulInstantiation(connectionPool, NOMINAL_POOL_SIZE);
  }

  public static void assertSuccessfulInstantiationWithPoolSize(int poolSize) {
    assertSuccessfulInstantiation(NOMINAL_CONNECTION_TIMEOUT, poolSize);
  }

  public static void assertInstantiationFailureWithInvalidConnectionTimeout(int connectionTimeout) {
    assertThatThrownBy(() -> new ConnectionPoolProperties(connectionTimeout, NOMINAL_POOL_SIZE))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The connection timeout must be between 1 and 600000");
  }

  public static void assertInstantiationFailureWithInvalidPoolSize(int invalidPoolSize) {
    assertThatThrownBy(
            () -> new ConnectionPoolProperties(NOMINAL_CONNECTION_TIMEOUT, invalidPoolSize))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The pool size must be between 1 and 100");
  }
}
