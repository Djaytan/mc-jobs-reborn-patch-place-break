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
package fr.djaytan.mc.jrppb.core.storage.sql.access;

import static org.mockito.BDDMockito.given;

import com.zaxxer.hikari.HikariDataSource;
import fr.djaytan.mc.jrppb.core.storage.api.DataSourceManager;
import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceProperties;
import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceType;
import fr.djaytan.mc.jrppb.core.storage.sql.ConnectionPool;
import fr.djaytan.mc.jrppb.core.storage.sql.DataMigrationExecutor;
import fr.djaytan.mc.jrppb.core.storage.sql.DataSourcePropertiesMock;
import fr.djaytan.mc.jrppb.core.storage.sql.JdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.SqlDataSourceManager;
import fr.djaytan.mc.jrppb.core.storage.sql.impl.mysql.MysqlJdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.provider.FlywayProvider;
import fr.djaytan.mc.jrppb.core.storage.sql.provider.HikariDataSourceProvider;
import fr.djaytan.mc.jrppb.core.storage.sql.serializer.BooleanIntegerSerializer;
import fr.djaytan.mc.jrppb.core.storage.sql.serializer.LocalDateTimeStringSerializer;
import javax.sql.DataSource;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

final class MysqlTagRepositoryFactory implements AutoCloseable {

  private DataSourceManager dataSourceManager;

  @NotNull
  SqlTagRepository create() {
    Validate.validState(dataSourceManager == null, "Creation already requested.");

    DataSourceProperties mysqlDataSourcePropertiesMock = createMysqlDataSourcePropertiesMock();
    HikariDataSource hikariDataSource = createHikariDataSource(mysqlDataSourcePropertiesMock);
    ConnectionPool connectionPool = new ConnectionPool(hikariDataSource);

    dataSourceManager =
        createDataSourceManager(connectionPool, hikariDataSource, mysqlDataSourcePropertiesMock);
    dataSourceManager.connect();

    TagSqlDao tagSqlDao = createTagSqlDao(mysqlDataSourcePropertiesMock);
    return new SqlTagRepository(connectionPool, tagSqlDao);
  }

  @Override
  public void close() {
    dataSourceManager.disconnect();
  }

  private static @NotNull DataSourceProperties createMysqlDataSourcePropertiesMock() {
    DataSourceProperties dataSourcePropertiesMock =
        DataSourcePropertiesMock.get(DataSourceType.MYSQL);
    int dbmsPort = Integer.parseInt(System.getProperty("mysql.port"));
    given(dataSourcePropertiesMock.getDbmsServer().getHost().getPort()).willReturn(dbmsPort);
    return dataSourcePropertiesMock;
  }

  private static @NotNull HikariDataSource createHikariDataSource(
      @NotNull DataSourceProperties dataSourceProperties) {
    JdbcUrl jdbcUrl = new MysqlJdbcUrl(dataSourceProperties);
    HikariDataSourceProvider hikariDataSourceProvider =
        new HikariDataSourceProvider(dataSourceProperties, jdbcUrl);
    return hikariDataSourceProvider.get();
  }

  private static @NotNull DataSourceManager createDataSourceManager(
      @NotNull ConnectionPool connectionPool,
      @NotNull DataSource dataSource,
      @NotNull DataSourceProperties dataSourceProperties) {
    DataMigrationExecutor dataMigrationExecutor =
        createDataMigrationExecutor(dataSource, dataSourceProperties);
    return new SqlDataSourceManager(connectionPool, dataMigrationExecutor);
  }

  private static @NotNull DataMigrationExecutor createDataMigrationExecutor(
      @NotNull DataSource dataSource, @NotNull DataSourceProperties dataSourceProperties) {
    ClassLoader classLoader = DataMigrationExecutor.class.getClassLoader();
    FlywayProvider flywayProvider =
        new FlywayProvider(classLoader, dataSource, dataSourceProperties);
    Flyway flyway = flywayProvider.get();
    return new DataMigrationExecutor(flyway);
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
