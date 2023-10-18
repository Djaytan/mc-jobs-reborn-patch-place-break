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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.ConnectionPoolProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import java.util.Objects;
import java.util.StringJoiner;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public final class ConnectionPoolPropertiesImpl implements ConnectionPoolProperties, Properties {

  @Max(600000)
  @Positive
  @Required
  @Comment(
      """
          The connection timeout (in milliseconds)
          Corresponds to the maximum time the connection pool will wait to acquire a new connection
          from the DBMS server
          Not applicable for SQLite
          Accepted range values: [1-600000]""")
  private final long connectionTimeout;

  @Max(100)
  @Positive
  @Required
  @Comment(
      """
          The number of DBMS connections in the pool
          Could be best determined by the executing environment
          Accepted range values: [1-100]""")
  private final int poolSize;

  public ConnectionPoolPropertiesImpl() {
    this.connectionTimeout = 30000;
    this.poolSize = 10;
  }

  /** Testing purposes only. */
  public ConnectionPoolPropertiesImpl(long connectionTimeout, int poolSize) {
    this.connectionTimeout = connectionTimeout;
    this.poolSize = poolSize;
  }

  @Override
  public long getConnectionTimeout() {
    return connectionTimeout;
  }

  @Override
  public int getPoolSize() {
    return poolSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ConnectionPoolPropertiesImpl that = (ConnectionPoolPropertiesImpl) o;
    return connectionTimeout == that.connectionTimeout && poolSize == that.poolSize;
  }

  @Override
  public int hashCode() {
    return Objects.hash(connectionTimeout, poolSize);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ConnectionPoolPropertiesImpl.class.getSimpleName() + "[", "]")
        .add("connectionTimeout=" + connectionTimeout)
        .add("poolSize=" + poolSize)
        .toString();
  }
}
