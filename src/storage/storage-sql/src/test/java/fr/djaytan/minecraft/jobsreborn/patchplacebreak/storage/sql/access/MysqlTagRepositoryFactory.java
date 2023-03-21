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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.access;

import static org.mockito.BDDMockito.given;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.ConnectionPool;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.DataSourcePropertiesMock;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlDataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.mysql.MysqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.mysql.MysqlJdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataMigrationExecutor;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.LocalDateTimeStringSerializer;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.api.Location;
import org.jetbrains.annotations.NotNull;

final class MysqlTagRepositoryFactory implements AutoCloseable {

  private DataSourceManager dataSourceManager;

  @NotNull
  SqlTagRepository create() {
    Validate.validState(dataSourceManager == null, "Creation already requested.");

    DataSourceProperties dataSourceProperties = createDataSourceProperties();
    ConnectionPool connectionPool = createConnectionPool(dataSourceProperties);

    dataSourceManager = createDataSourceManager(connectionPool, dataSourceProperties);
    dataSourceManager.connect();

    TagSqlDao tagSqlDao = createTagSqlDao(dataSourceProperties);
    return new SqlTagRepository(connectionPool, tagSqlDao);
  }

  @Override
  public void close() {
    dataSourceManager.disconnect();
  }

  private static @NotNull DataSourceProperties createDataSourceProperties() {
    DataSourceProperties dataSourcePropertiesMock = DataSourcePropertiesMock.get();
    int dbmsPort = Integer.parseInt(System.getProperty("mysql.port"));
    given(dataSourcePropertiesMock.getDbmsServer().getHost().getPort()).willReturn(dbmsPort);
    return dataSourcePropertiesMock;
  }

  private static @NotNull ConnectionPool createConnectionPool(
      @NotNull DataSourceProperties dataSourceProperties) {
    JdbcUrl jdbcUrl = new MysqlJdbcUrl(dataSourceProperties);
    return new ConnectionPool(dataSourceProperties, jdbcUrl);
  }

  private static @NotNull DataSourceManager createDataSourceManager(
      @NotNull ConnectionPool connectionPool, @NotNull DataSourceProperties dataSourceProperties) {
    DataMigrationExecutor dataMigrationExecutor = createDataMigrationExecutor(dataSourceProperties);
    DataSourceInitializer dataSourceInitializer = new MysqlDataSourceInitializer();
    return new SqlDataSourceManager(connectionPool, dataMigrationExecutor, dataSourceInitializer);
  }

  private static @NotNull DataMigrationExecutor createDataMigrationExecutor(
      @NotNull DataSourceProperties dataSourceProperties) {
    ClassLoader classLoader = DataMigrationExecutor.class.getClassLoader();
    Location location = new Location("/db/migration/mysql");
    return new DataMigrationExecutor(classLoader, dataSourceProperties, location);
  }

  private static @NotNull TagSqlDao createTagSqlDao(
      @NotNull DataSourceProperties dataSourceProperties) {
    BooleanIntegerSerializer booleanIntegerSerializer = new BooleanIntegerSerializer();
    LocalDateTimeStringSerializer localDateTimeStringSerializer =
        new LocalDateTimeStringSerializer();
    return new TagSqlDao(
        booleanIntegerSerializer, dataSourceProperties, localDateTimeStringSerializer);
  }
}
