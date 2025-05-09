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

import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerCredentialsConfigPropertiesTestDataSet.NOMINAL_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerCredentialsConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerHostConfigPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerHostConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_HOST_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_DATABASE_NAME;

public final class DbmsServerConfigPropertiesTestDataSet {

  public static final DbmsServerConfigProperties NOMINAL_DBMS_SERVER_CONFIG_PROPERTIES =
      new DbmsServerConfigProperties(
          NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES,
          NOMINAL_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES,
          NOMINAL_DBMS_SERVER_DATABASE_NAME);

  public static final String NOMINAL_SERIALIZED_DBMS_SERVER_CONFIG_PROPERTIES =
      """
      # Host properties of the DBMS server
      host {
          %s
      }
      # Credentials for authentication with the DBMS server
      credentials {
          %s
      }
      # The database to use on DBMS server
      # Value can't be empty or blank
      database=minecraft
      """
          .formatted(
              NOMINAL_SERIALIZED_DBMS_SERVER_HOST_CONFIG_PROPERTIES.indent(4).trim(),
              NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES.indent(4).trim());
}
