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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.mc.jrppb.core.storage.properties.DataSourceProperties;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.JdbcUrl;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HikariDataSourceProvider implements Provider<HikariDataSource> {

  private static final Logger LOG = LoggerFactory.getLogger(HikariDataSourceProvider.class);

  private final DataSourceProperties dataSourceProperties;
  private final JdbcUrl jdbcUrl;

  @Inject
  public HikariDataSourceProvider(
      @NotNull DataSourceProperties dataSourceProperties, @NotNull JdbcUrl jdbcUrl) {
    this.dataSourceProperties = dataSourceProperties;
    this.jdbcUrl = jdbcUrl;
  }

  public @NotNull HikariDataSource get() {
    LOG.atInfo().log("Connecting to database '{}'...", jdbcUrl.get());
    HikariDataSource hikariDataSource = new HikariDataSource(createHikariConfig());
    LOG.atInfo().log("Connected to the database successfully.");
    return hikariDataSource;
  }

  private @NotNull HikariConfig createHikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcUrl.get());
    hikariConfig.setConnectionTimeout(dataSourceProperties.connectionPool().connectionTimeout());
    hikariConfig.setMaximumPoolSize(dataSourceProperties.connectionPool().poolSize());
    hikariConfig.setUsername(dataSourceProperties.dbmsServer().credentials().username());
    hikariConfig.setPassword(dataSourceProperties.dbmsServer().credentials().password());
    return hikariConfig;
  }
}
