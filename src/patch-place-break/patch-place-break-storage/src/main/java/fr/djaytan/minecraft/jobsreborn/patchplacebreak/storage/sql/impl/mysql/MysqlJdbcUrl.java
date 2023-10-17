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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.mysql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsServerProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class MysqlJdbcUrl implements JdbcUrl {

  private static final String MYSQL_JDBC_URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=%s";
  private static final String SERVER_TIME_ZONE = "UTC";

  private final DataSourceProperties dataSourceProperties;

  @Inject
  public MysqlJdbcUrl(@NotNull DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  @Override
  public @NotNull String get() {
    DbmsServerProperties dbmsServerProperties = dataSourceProperties.getDbmsServer();
    String hostname = dbmsServerProperties.getHost().getHostname();
    int port = dbmsServerProperties.getHost().getPort();
    String database = dbmsServerProperties.getDatabase();
    boolean isSslEnabled = dbmsServerProperties.getHost().isSslEnabled();
    return String.format(
        MYSQL_JDBC_URL_TEMPLATE, hostname, port, database, isSslEnabled, SERVER_TIME_ZONE);
  }
}
