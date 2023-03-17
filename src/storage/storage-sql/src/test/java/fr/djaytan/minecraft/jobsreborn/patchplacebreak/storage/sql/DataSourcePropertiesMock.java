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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsCredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsServerProperties;
import lombok.NonNull;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;

public final class DataSourcePropertiesMock {

  private static final MockSettings MOCK_SETTINGS =
      Mockito.withSettings().strictness(Strictness.LENIENT);

  private DataSourcePropertiesMock() {
    // Static class
  }

  public static @NonNull DataSourceProperties get() {
    ConnectionPoolProperties connectionPoolPropertiesMocked = connectionPoolPropertiesMocked();
    DbmsServerProperties dbmsServerPropertiesMocked = dbmsServerPropertiesMocked();

    DataSourceProperties dataSourcePropertiesMocked =
        mock(DataSourceProperties.class, MOCK_SETTINGS);
    given(dataSourcePropertiesMocked.getTable()).willReturn("patch_place_break_tag");
    given(dataSourcePropertiesMocked.getType()).willReturn(DataSourceType.SQLITE);
    given(dataSourcePropertiesMocked.getConnectionPool())
        .willReturn(connectionPoolPropertiesMocked);
    given(dataSourcePropertiesMocked.getDbmsServer()).willReturn(dbmsServerPropertiesMocked);
    return dataSourcePropertiesMocked;
  }

  private static @NonNull ConnectionPoolProperties connectionPoolPropertiesMocked() {
    ConnectionPoolProperties connectionPoolPropertiesMocked =
        mock(ConnectionPoolProperties.class, MOCK_SETTINGS);
    given(connectionPoolPropertiesMocked.getConnectionTimeout()).willReturn(30000L);
    given(connectionPoolPropertiesMocked.getPoolSize()).willReturn(10);
    return connectionPoolPropertiesMocked;
  }

  private static @NonNull DbmsServerProperties dbmsServerPropertiesMocked() {
    DbmsCredentialsProperties dbmsCredentialsPropertiesMocked = dbmsCredentialsProperties();
    DbmsHostProperties dbmsHostPropertiesMocked = dbmsHostPropertiesMocked();

    DbmsServerProperties dbmsServerPropertiesMocked =
        mock(DbmsServerProperties.class, MOCK_SETTINGS);
    given(dbmsServerPropertiesMocked.getDatabase()).willReturn("patch_place_break");
    given(dbmsServerPropertiesMocked.getCredentials()).willReturn(dbmsCredentialsPropertiesMocked);
    given(dbmsServerPropertiesMocked.getHost()).willReturn(dbmsHostPropertiesMocked);
    return dbmsServerPropertiesMocked;
  }

  private static @NonNull DbmsCredentialsProperties dbmsCredentialsProperties() {
    DbmsCredentialsProperties dbmsCredentialsPropertiesMocked =
        mock(DbmsCredentialsProperties.class, MOCK_SETTINGS);
    given(dbmsCredentialsPropertiesMocked.getUsername()).willReturn("root");
    given(dbmsCredentialsPropertiesMocked.getPassword()).willReturn("");
    return dbmsCredentialsPropertiesMocked;
  }

  private static @NonNull DbmsHostProperties dbmsHostPropertiesMocked() {
    DbmsHostProperties dbmsHostPropertiesMocked = mock(DbmsHostProperties.class, MOCK_SETTINGS);
    given(dbmsHostPropertiesMocked.getHostname()).willReturn("localhost");
    given(dbmsHostPropertiesMocked.getPort()).willReturn(3306);
    given(dbmsHostPropertiesMocked.isSslEnabled()).willReturn(true);
    return dbmsHostPropertiesMocked;
  }
}
