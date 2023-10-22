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
package fr.djaytan.mc.jrppb.core.storage.sql.provider;

import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceProperties;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.JdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.MysqlJdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.SqliteJdbcUrl;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.jetbrains.annotations.NotNull;

public class JdbcUrlProvider implements Provider<JdbcUrl> {

  private final DataSourceProperties dataSourceProperties;
  private final MysqlJdbcUrl mysqlJdbcUrl;
  private final SqliteJdbcUrl sqliteJdbcUrl;

  @Inject
  public JdbcUrlProvider(
      @NotNull DataSourceProperties dataSourceProperties,
      @NotNull MysqlJdbcUrl mysqlJdbcUrl,
      @NotNull SqliteJdbcUrl sqliteJdbcUrl) {
    this.dataSourceProperties = dataSourceProperties;
    this.mysqlJdbcUrl = mysqlJdbcUrl;
    this.sqliteJdbcUrl = sqliteJdbcUrl;
  }

  @Override
  public @NotNull JdbcUrl get() {
    return switch (dataSourceProperties.getType()) {
      case MYSQL -> mysqlJdbcUrl;
      case SQLITE -> sqliteJdbcUrl;
    };
  }
}
