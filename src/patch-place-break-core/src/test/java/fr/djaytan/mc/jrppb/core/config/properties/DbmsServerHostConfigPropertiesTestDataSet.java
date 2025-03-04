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

import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOSTNAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_IS_SSL_ENABLED;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PORT;

public final class DbmsServerHostConfigPropertiesTestDataSet {

  public static final DbmsServerHostConfigProperties NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES =
      new DbmsServerHostConfigProperties(
          NOMINAL_DBMS_SERVER_HOSTNAME,
          NOMINAL_DBMS_SERVER_PORT,
          NOMINAL_DBMS_SERVER_IS_SSL_ENABLED);

  public static final String NOMINAL_SERIALIZED_DBMS_SERVER_HOST_CONFIG_PROPERTIES =
      """
      # Hostname (an IP address (IPv4/IPv6) or a domain name)
      # Value can't be empty or blank
      hostname="db.amazing.com"
      # Port
      # Accepted range values: [1-65535]
      port=4123
      # Whether an SSL/TLS communication must be established at connection time (more secure)
      # Only boolean values accepted (true|false)
      isSslEnabled=true
      """;
}
