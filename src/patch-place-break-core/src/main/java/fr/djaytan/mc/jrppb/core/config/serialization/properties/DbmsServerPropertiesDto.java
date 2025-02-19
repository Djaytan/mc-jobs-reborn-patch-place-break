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
package fr.djaytan.mc.jrppb.core.config.serialization.properties;

import fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record DbmsServerPropertiesDto(
    @Required @Comment(HOST_COMMENT) @NotNull DbmsServerHostPropertiesDto host,
    @Required @Comment(CREDENTIALS_COMMENT) @NotNull DbmsServerCredentialsPropertiesDto credentials,
    @Required @Comment(DATABASE_COMMENT) @NotNull String database) {

  private static final String HOST_COMMENT = "Host properties of the DBMS server";
  private static final String CREDENTIALS_COMMENT =
      "Credentials for authentication with the DBMS server";
  private static final String DATABASE_COMMENT =
      """
      The database to use on DBMS server
      Value can't be empty or blank""";

  public static @NotNull DbmsServerPropertiesDto fromModel(@NotNull DbmsServerProperties model) {
    return new DbmsServerPropertiesDto(
        DbmsServerHostPropertiesDto.fromModel(model.host()),
        DbmsServerCredentialsPropertiesDto.fromModel(model.credentials()),
        model.databaseName());
  }

  public @NotNull DbmsServerProperties toModel() {
    return new DbmsServerProperties(host.toModel(), credentials.toModel(), database);
  }
}
