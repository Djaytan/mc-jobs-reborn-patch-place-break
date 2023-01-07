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

import java.lang.reflect.Type;

import javax.inject.Singleton;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;

@Singleton
final class DataSourcePropertiesSerializer implements TypeSerializer<DataSourceProperties> {

  private static final String TYPE = "type";
  private static final String TABLE = "table";
  private static final String DBMS_SERVER = "dbmsServer";
  private static final String CONNECTION_POOL = "connectionPool";

  @Override
  public DataSourceProperties deserialize(Type type, ConfigurationNode node)
      throws SerializationException {
    DataSourceType dataSourceType = node.node(TYPE).require(DataSourceType.class);
    String table = node.node(TABLE).require(String.class);
    DbmsServerProperties dbmsServer = node.node(DBMS_SERVER).require(DbmsServerProperties.class);
    ConnectionPoolProperties connectionPool =
        node.node(CONNECTION_POOL).require(ConnectionPoolProperties.class);
    return DataSourceProperties.of(dataSourceType, table, dbmsServer, connectionPool);
  }

  @Override
  public void serialize(Type type, @Nullable DataSourceProperties obj, ConfigurationNode node) {
    throw ConfigSerializationException.unexpectedSerialization();
  }
}
