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

import static fr.djaytan.mc.jrppb.core.config.properties.ConnectionPoolConfigPropertiesTestDataSet.NOMINAL_CONNECTION_POOL_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.ConnectionPoolConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_CONNECTION_POOL_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerConfigPropertiesTestDataSet.NOMINAL_DBMS_SERVER_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_DATA_SOURCE_TABLE_NAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_DATA_SOURCE_TYPE;

public final class DataSourceConfigPropertiesTestDataSet {

  public static final DataSourceConfigProperties NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES =
      new DataSourceConfigProperties(
          NOMINAL_DATA_SOURCE_TYPE,
          NOMINAL_DATA_SOURCE_TABLE_NAME,
          NOMINAL_DBMS_SERVER_CONFIG_PROPERTIES,
          NOMINAL_CONNECTION_POOL_CONFIG_PROPERTIES);

  public static final String NOMINAL_SERIALIZED_DATA_SOURCE_CONFIG_PROPERTIES =
      """
      # The type of datasource to use
      # Available types:
      # * SQLITE: use a local file as database (easy & fast setup)
      # * MYSQL: use a MySQL database server (better performances)
      type=MYSQL
      # The table where data will be stored
      # Value can't be empty or blank
      table="nominal_table_name"
      # The DBMS server properties for connection establishment
      # Not applicable for SQLite
      dbmsServer {
          %s
      }
      # Connection pool properties
      # This is reserved for advanced usage only
      # Change these settings only if you know what you are doing
      connectionPool {
          %s
      }
      """
          .formatted(
              NOMINAL_SERIALIZED_DBMS_SERVER_CONFIG_PROPERTIES.indent(4).trim(),
              NOMINAL_SERIALIZED_CONNECTION_POOL_CONFIG_PROPERTIES.indent(4).trim());
}
