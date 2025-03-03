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

import static com.google.common.jimfs.Configuration.unix;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_MYSQL_DATA_SOURCE_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_SQLITE_DATA_SOURCE_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Jimfs;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.MysqlJdbcUrl;
import fr.djaytan.mc.jrppb.core.storage.sql.jdbc.SqliteJdbcUrl;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;

final class JdbcUrlProviderTest {

  @AutoClose private final FileSystem imfs = Jimfs.newFileSystem(unix());
  private final Path sqliteDbFile = imfs.getPath("sqlite-data.db");
  private final SqliteJdbcUrl sqliteJdbcUrl = new SqliteJdbcUrl(sqliteDbFile);

  private final MysqlJdbcUrl mysqlJdbcUrl = new MysqlJdbcUrl(NOMINAL_MYSQL_DATA_SOURCE_PROPERTIES);

  @Test
  void mysqlJdbcUrl() {
    assertThat(
            new JdbcUrlProvider(NOMINAL_MYSQL_DATA_SOURCE_PROPERTIES, mysqlJdbcUrl, sqliteJdbcUrl)
                .get())
        .isExactlyInstanceOf(MysqlJdbcUrl.class);
  }

  @Test
  void sqliteJdbcUrl() {
    assertThat(
            new JdbcUrlProvider(NOMINAL_SQLITE_DATA_SOURCE_PROPERTIES, mysqlJdbcUrl, sqliteJdbcUrl)
                .get())
        .isExactlyInstanceOf(SqliteJdbcUrl.class);
  }
}
