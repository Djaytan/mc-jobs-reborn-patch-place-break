/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ConnectionPool {

  protected final DataSourceProperties dataSourceProperties;
  private HikariDataSource hikariDataSource;

  protected ConnectionPool(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  public void connect() {
    String jdbcUrl = getJdbcUrl();

    log.atInfo().log("Connecting to '{}'...", jdbcUrl);

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcUrl);
    hikariConfig
        .setConnectionTimeout(dataSourceProperties.getConnectionPool().getConnectionTimeout());
    hikariConfig.setMaximumPoolSize(dataSourceProperties.getConnectionPool().getPoolSize());
    hikariConfig.setUsername(dataSourceProperties.getDbmsServer().getCredentials().getUsername());
    hikariConfig.setPassword(dataSourceProperties.getDbmsServer().getCredentials().getPassword());
    hikariDataSource = new HikariDataSource(hikariConfig);

    log.atInfo().log("Connected to the database successfully.");
  }

  public void disconnect() {
    if (hikariDataSource == null) {
      log.atWarn().log("Database disconnection impossible: no existing connection.");
      return;
    }
    if (hikariDataSource.isClosed()) {
      log.atWarn().log("Database disconnection impossible: connection already closed.");
      return;
    }
    hikariDataSource.close();
    log.atInfo().log("Disconnected from the database '{}'.", hikariDataSource.getJdbcUrl());
  }

  public @NonNull Connection getConnection() {
    if (hikariDataSource == null) {
      throw SqlStorageException.connectionPoolNotSetup();
    }

    try {
      return hikariDataSource.getConnection();
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionEstablishment(e);
    }
  }

  protected abstract @NonNull String getJdbcUrl();
}
