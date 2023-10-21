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
package fr.djaytan.mc.jrppb.core.storage.sql.impl.mysql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceProperties;
import fr.djaytan.mc.jrppb.core.storage.api.properties.DbmsHostProperties;
import fr.djaytan.mc.jrppb.core.storage.api.properties.DbmsServerProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MysqlJdbcUrlTest {

  @Mock private DataSourceProperties dataSourcePropertiesMocked;
  @Mock private DbmsServerProperties dbmsServerPropertiesMocked;
  @Mock private DbmsHostProperties dbmsHostPropertiesMocked;

  @Test
  void whenGettingJdbcUrlFromConfig_shouldReturnExpectedMySQLJdbcUrl() {
    // Given
    given(dataSourcePropertiesMocked.getDbmsServer()).willReturn(dbmsServerPropertiesMocked);
    given(dbmsServerPropertiesMocked.getHost()).willReturn(dbmsHostPropertiesMocked);
    given(dbmsServerPropertiesMocked.getDatabase()).willReturn("patch_place_break");
    given(dbmsHostPropertiesMocked.getHostname()).willReturn("localhost");
    given(dbmsHostPropertiesMocked.getPort()).willReturn(12345);
    given(dbmsHostPropertiesMocked.isSslEnabled()).willReturn(true);

    MysqlJdbcUrl mysqlJdbcUrl = new MysqlJdbcUrl(dataSourcePropertiesMocked);

    // When
    String actualMysqlJdbcUrl = mysqlJdbcUrl.get();

    // Then
    assertThat(actualMysqlJdbcUrl)
        .isEqualTo("jdbc:mysql://localhost:12345/patch_place_break?useSSL=true&serverTimezone=UTC");
  }
}
