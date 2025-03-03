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

import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_POOL_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_DATABASE_NAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PROPERTIES;

import org.instancio.Instancio;
import org.jetbrains.annotations.NotNull;

public final class DataSourcePropertiesTestDataSet {

  public static final DataSourceType NOMINAL_DATA_SOURCE_TYPE = DataSourceType.MYSQL;
  public static final String NOMINAL_DATA_SOURCE_TABLE_NAME = "nominal_table_name";

  public static final DataSourceProperties NOMINAL_DATA_SOURCE_PROPERTIES =
      new DataSourceProperties(
          NOMINAL_DATA_SOURCE_TYPE,
          NOMINAL_DATA_SOURCE_TABLE_NAME,
          NOMINAL_DBMS_SERVER_PROPERTIES,
          NOMINAL_CONNECTION_POOL_PROPERTIES);

  public static final DataSourceProperties NOMINAL_SQLITE_DATA_SOURCE_PROPERTIES =
      new DataSourceProperties(
          DataSourceType.SQLITE,
          NOMINAL_DATA_SOURCE_TABLE_NAME,
          NOMINAL_DBMS_SERVER_PROPERTIES,
          NOMINAL_CONNECTION_POOL_PROPERTIES);
  public static final DataSourceProperties NOMINAL_MYSQL_DATA_SOURCE_PROPERTIES =
      new DataSourceProperties(
          DataSourceType.MYSQL,
          NOMINAL_DATA_SOURCE_TABLE_NAME,
          NOMINAL_DBMS_SERVER_PROPERTIES,
          NOMINAL_CONNECTION_POOL_PROPERTIES);

  public static @NotNull DataSourceProperties nominalMysqlDataSourceProperties(
      @NotNull DbmsServerHostProperties hostProperties,
      @NotNull DbmsServerCredentialsProperties credentialsProperties) {
    return new DataSourceProperties(
        DataSourceType.MYSQL,
        NOMINAL_DATA_SOURCE_TABLE_NAME,
        new DbmsServerProperties(
            hostProperties, credentialsProperties, NOMINAL_DBMS_SERVER_DATABASE_NAME),
        NOMINAL_CONNECTION_POOL_PROPERTIES);
  }

  public static @NotNull DataSourceType randomDataSourceType() {
    return Instancio.create(DataSourceType.class);
  }

  public static @NotNull String randomDataSourceTableName() {
    return Instancio.gen().string().alphaNumeric().mixedCase().length(1, 64).get();
  }
}
