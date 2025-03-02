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
package fr.djaytan.mc.jrppb.core.config.properties_v2;

import fr.djaytan.mc.jrppb.core.storage.properties.DataSourceProperties;
import fr.djaytan.mc.jrppb.core.storage.properties.DataSourceType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record DataSourceConfigProperties(
    @Required @Comment(TYPE_COMMENT) @NotNull DataSourceType type,
    @Required @Comment(TABLE_COMMENT) @NotNull String table,
    @Required @Comment(DBMS_SERVER_COMMENT) @NotNull DbmsServerConfigProperties dbmsServer,
    @Required @Comment(CONNECTION_POOL_COMMENT) @NotNull
        ConnectionPoolConfigProperties connectionPool) {

  private static final String TYPE_COMMENT =
      """
      The type of datasource to use
      Available types:
      * SQLITE: use a local file as database (easy & fast setup)
      * MYSQL: use a MySQL database server (better performances)""";

  private static final String TABLE_COMMENT =
      """
      The table where data will be stored
      Value can't be empty or blank""";

  private static final String DBMS_SERVER_COMMENT =
      """
      The DBMS server properties for connection establishment
      Not applicable for SQLite""";

  private static final String CONNECTION_POOL_COMMENT =
      """
      Connection pool properties
      This is reserved for advanced usage only
      Change these settings only if you know what you are doing""";

  public static @NotNull DataSourceConfigProperties fromModel(@NotNull DataSourceProperties model) {
    return new DataSourceConfigProperties(
        model.type(),
        model.tableName(),
        DbmsServerConfigProperties.fromModel(model.dbmsServer()),
        ConnectionPoolConfigProperties.fromModel(model.connectionPool()));
  }

  public @NotNull DataSourceProperties toModel() {
    return new DataSourceProperties(type, table, dbmsServer.toModel(), connectionPool.toModel());
  }
}
