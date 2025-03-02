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

import fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record DbmsServerHostConfigProperties(
    @Required @Comment(HOSTNAME_COMMENT) @NotNull String hostname,
    @Required @Comment(PORT_COMMENT) int port,
    @Required @Comment(SSL_ENABLED_COMMENT) boolean isSslEnabled)
    implements ConfigProperties {

  private static final String HOSTNAME_COMMENT =
      """
      Hostname (an IP address (IPv4/IPv6) or a domain name)
      Value can't be empty or blank""";

  private static final String PORT_COMMENT =
      """
      Port
      Accepted range values: [1-65535]""";

  private static final String SSL_ENABLED_COMMENT =
      """
      Whether an SSL/TLS communication must be established at connection time (more secure)
      Only boolean values accepted (true|false)""";

  public static @NotNull DbmsServerHostConfigProperties fromModel(
      @NotNull DbmsServerHostProperties model) {
    return new DbmsServerHostConfigProperties(model.hostname(), model.port(), model.isSslEnabled());
  }

  public @NotNull DbmsServerHostProperties toModel() {
    return new DbmsServerHostProperties(hostname, port, isSslEnabled);
  }
}
