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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
@Value
public class DataSourcePropertiesImpl implements DataSourceProperties, Properties {

  @NotNull
  @Required
  @Comment(
      """
          The type of datasource to use
          Available types:
          * SQLITE: use a local file as database (easy & fast setup)
          * MYSQL: use a MySQL database server (better performances)""")
  DataSourceType type;

  @NotBlank
  @Size(max = 128)
  @Required
  @Comment("""
      The table where data will be stored
      Value can't be empty or blank""")
  String table;

  @NotNull
  @Valid
  @Required
  @Comment(
      """
          The DBMS server properties for connection establishment
          Not applicable for SQLite""")
  DbmsServerPropertiesImpl dbmsServer;

  @NotNull
  @Valid
  @Required
  @Comment(
      """
          Connection pool properties
          This is reserved for advanced usage only
          Change these settings only if you know what you are doing""")
  ConnectionPoolPropertiesImpl connectionPool;

  public DataSourcePropertiesImpl() {
    this.type = DataSourceType.SQLITE;
    this.table = "patch_place_break_tag";
    this.dbmsServer = new DbmsServerPropertiesImpl();
    this.connectionPool = new ConnectionPoolPropertiesImpl();
  }

  public DataSourcePropertiesImpl(
      @Nullable DataSourceType type,
      @Nullable String table,
      @Nullable DbmsServerPropertiesImpl dbmsServer,
      @Nullable ConnectionPoolPropertiesImpl connectionPool) {
    this.type = type;
    this.table = table;
    this.dbmsServer = dbmsServer;
    this.connectionPool = connectionPool;
  }
}
