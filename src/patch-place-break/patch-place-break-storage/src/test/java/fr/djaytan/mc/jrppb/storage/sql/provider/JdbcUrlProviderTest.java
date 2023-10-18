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
package fr.djaytan.mc.jrppb.storage.sql.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;

import fr.djaytan.mc.jrppb.storage.api.properties.DataSourceProperties;
import fr.djaytan.mc.jrppb.storage.api.properties.DataSourceType;
import fr.djaytan.mc.jrppb.storage.sql.JdbcUrl;
import fr.djaytan.mc.jrppb.storage.sql.impl.mysql.MysqlJdbcUrl;
import fr.djaytan.mc.jrppb.storage.sql.impl.sqlite.SqliteJdbcUrl;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JdbcUrlProviderTest {

  @Mock private DataSourceProperties dataSourceProperties;
  @Mock private MysqlJdbcUrl mysqlJdbcUrl;
  @Mock private SqliteJdbcUrl sqliteJdbcUrl;

  @ParameterizedTest(name = "{index} - With {0} data source type")
  @MethodSource
  @DisplayName("When providing JDBC URL")
  void whenProvidingJdbcUrl(
      @NotNull DataSourceType dataSourceType, Class<? extends JdbcUrl> expectedJdbcUrlType) {
    // Given
    given(dataSourceProperties.getType()).willReturn(dataSourceType);
    JdbcUrlProvider jdbcUrlProvider =
        new JdbcUrlProvider(dataSourceProperties, mysqlJdbcUrl, sqliteJdbcUrl);

    // When
    JdbcUrl jdbcUrl = jdbcUrlProvider.get();

    // Then
    assertThat(jdbcUrl).isExactlyInstanceOf(expectedJdbcUrlType);
  }

  private static @NotNull Stream<Arguments> whenProvidingJdbcUrl() {
    return Stream.of(
        arguments(named(DataSourceType.SQLITE.name(), DataSourceType.SQLITE), SqliteJdbcUrl.class),
        arguments(named(DataSourceType.MYSQL.name(), DataSourceType.MYSQL), MysqlJdbcUrl.class));
  }
}
