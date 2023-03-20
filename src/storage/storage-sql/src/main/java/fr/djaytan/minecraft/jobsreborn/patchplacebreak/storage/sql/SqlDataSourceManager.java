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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataMigrationExecutor;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SqlDataSourceManager implements DataSourceManager {

  private final ConnectionPool connectionPool;
  private final DataMigrationExecutor dataMigrationExecutor;
  private final DataSourceInitializer dataSourceInitializer;

  @Inject
  public SqlDataSourceManager(
      @NotNull ConnectionPool connectionPool,
      @NotNull DataMigrationExecutor dataMigrationExecutor,
      @NotNull DataSourceInitializer dataSourceInitializer) {
    this.connectionPool = connectionPool;
    this.dataMigrationExecutor = dataMigrationExecutor;
    this.dataSourceInitializer = dataSourceInitializer;
  }

  @Override
  public void connect() {
    dataSourceInitializer.initialize();

    connectionPool.enable();
    DataSource dataSource = connectionPool.getDataSource();

    dataMigrationExecutor.migrate(dataSource);
  }

  @Override
  public void disconnect() {
    connectionPool.disable();
  }
}
