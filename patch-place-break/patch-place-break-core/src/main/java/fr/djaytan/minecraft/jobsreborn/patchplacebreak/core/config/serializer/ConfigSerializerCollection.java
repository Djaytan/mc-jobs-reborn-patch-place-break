/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.Config;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.HostProperties;
import lombok.NonNull;

@Singleton
public class ConfigSerializerCollection {

  private final ConfigSerializer configSerializer;
  private final ConnectionPoolPropertiesSerializer connectionPoolPropertiesSerializer;
  private final DataSourcePropertiesSerializer dataSourcePropertiesSerializer;
  private final DbmsServerPropertiesSerializer dbmsServerPropertiesSerializer;
  private final HostPropertiesSerializer hostPropertiesSerializer;
  private final CredentialsPropertiesSerializer credentialsPropertiesSerializer;

  @Inject
  ConfigSerializerCollection(ConfigSerializer configSerializer,
      ConnectionPoolPropertiesSerializer connectionPoolPropertiesSerializer,
      DataSourcePropertiesSerializer dataSourcePropertiesSerializer,
      DbmsServerPropertiesSerializer dbmsServerPropertiesSerializer,
      HostPropertiesSerializer hostPropertiesSerializer,
      CredentialsPropertiesSerializer credentialsPropertiesSerializer) {
    this.configSerializer = configSerializer;
    this.connectionPoolPropertiesSerializer = connectionPoolPropertiesSerializer;
    this.dataSourcePropertiesSerializer = dataSourcePropertiesSerializer;
    this.dbmsServerPropertiesSerializer = dbmsServerPropertiesSerializer;
    this.hostPropertiesSerializer = hostPropertiesSerializer;
    this.credentialsPropertiesSerializer = credentialsPropertiesSerializer;
  }

  public @NonNull TypeSerializerCollection collection() {
    return TypeSerializerCollection.builder().registerExact(Config.class, configSerializer)
        .registerExact(DataSourceProperties.class, dataSourcePropertiesSerializer)
        .registerExact(DbmsServerProperties.class, dbmsServerPropertiesSerializer)
        .registerExact(ConnectionPoolProperties.class, connectionPoolPropertiesSerializer)
        .registerExact(HostProperties.class, hostPropertiesSerializer)
        .registerExact(CredentialsProperties.class, credentialsPropertiesSerializer).build();
  }
}
