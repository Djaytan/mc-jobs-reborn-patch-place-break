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

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsCredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsServerProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.StringJoiner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public final class DbmsServerPropertiesImpl implements DbmsServerProperties, Properties {

  @NotNull
  @Valid
  @Required
  @Comment("Host properties of the DBMS server")
  private final DbmsHostPropertiesImpl host;

  @NotNull
  @Valid
  @Required
  @Comment("Credentials for authentication with the DBMS server")
  private final DbmsCredentialsPropertiesImpl credentials;

  @NotBlank
  @Size(max = 128)
  @Required
  @Comment("""
      The database to use on DBMS server
      Value can't be empty or blank""")
  private final String database;

  public DbmsServerPropertiesImpl() {
    this.host = new DbmsHostPropertiesImpl();
    this.credentials = new DbmsCredentialsPropertiesImpl();
    this.database = "database";
  }

  /** Testing purposes only. */
  public DbmsServerPropertiesImpl(
      @Nullable DbmsHostPropertiesImpl host,
      @Nullable DbmsCredentialsPropertiesImpl credentials,
      @Nullable String database) {
    this.host = host;
    this.credentials = credentials;
    this.database = database;
  }

  @Override
  public @org.jetbrains.annotations.NotNull DbmsHostProperties getHost() {
    return Objects.requireNonNull(host);
  }

  @Override
  public @org.jetbrains.annotations.NotNull DbmsCredentialsProperties getCredentials() {
    return Objects.requireNonNull(credentials);
  }

  @Override
  public @org.jetbrains.annotations.NotNull String getDatabase() {
    return Objects.requireNonNull(database);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DbmsServerPropertiesImpl that = (DbmsServerPropertiesImpl) o;
    return Objects.equals(host, that.host)
        && Objects.equals(credentials, that.credentials)
        && Objects.equals(database, that.database);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, credentials, database);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DbmsServerPropertiesImpl.class.getSimpleName() + "[", "]")
        .add("host=" + host)
        .add("credentials=" + credentials)
        .add("database='" + database + "'")
        .toString();
  }
}
