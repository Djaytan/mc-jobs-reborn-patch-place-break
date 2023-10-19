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
package fr.djaytan.mc.jrppb.core.config.properties;

import fr.djaytan.mc.jrppb.core.storage.api.properties.DbmsHostProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.StringJoiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public final class DbmsHostPropertiesImpl implements DbmsHostProperties, Properties {

  /**
   * A hostname cannot exceed 255 characters as per the DNS standard specification.
   *
   * <p><i/>Note: we explicitly allow most invalid hostnames since otherwise it will require too
   * much work for too limited earnings. In fact, specifying an invalid hostname will be detected by
   * underlying systems (e.g. JDBC). So, we only focus on easy detectable invalid addresses.
   */
  @NotBlank
  @Size(max = 255)
  @Required
  @Comment(
      """
          Hostname (an IP address (IPv4/IPv6) or a domain name)
          Value can't be empty or blank""")
  private final String hostname;

  /**
   * A port cannot exceed 65535, which is the maximum value allowed by the Transport Control
   * Protocol (TCP) and User Datagram Protocol (UDP) standards. The value "0" is excluded since it's
   * a reserved one and must not be used.
   */
  @Max(65535)
  @Positive
  @Required
  @Comment("""
      Port
      Accepted range values: [1-65535]""")
  private final int port;

  @Required
  @Comment(
      """
          Whether an SSL/TLS communication must be established at connection time (more secure)
          Only boolean values accepted (true|false)""")
  private final boolean isSslEnabled;

  public DbmsHostPropertiesImpl() {
    this.hostname = "localhost";
    this.port = 3306;
    this.isSslEnabled = true;
  }

  /** Testing purposes only. */
  public DbmsHostPropertiesImpl(@Nullable String hostname, int port, boolean isSslEnabled) {
    this.hostname = hostname;
    this.port = port;
    this.isSslEnabled = isSslEnabled;
  }

  @Override
  public @NotNull String getHostname() {
    return Objects.requireNonNull(hostname);
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public boolean isSslEnabled() {
    return isSslEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DbmsHostPropertiesImpl that = (DbmsHostPropertiesImpl) o;
    return port == that.port
        && isSslEnabled == that.isSslEnabled
        && Objects.equals(hostname, that.hostname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hostname, port, isSslEnabled);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DbmsHostPropertiesImpl.class.getSimpleName() + "[", "]")
        .add("hostname='" + hostname + "'")
        .add("port=" + port)
        .add("isSslEnabled=" + isSslEnabled)
        .toString();
  }
}
