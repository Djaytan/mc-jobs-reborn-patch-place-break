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
package fr.djaytan.mc.jrppb.core.config.properties;

import fr.djaytan.mc.jrppb.core.storage.api.properties.DbmsCredentialsProperties;
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
public final class DbmsCredentialsPropertiesImpl implements DbmsCredentialsProperties, Properties {

  @NotBlank
  @Size(max = 32)
  @Required
  @Comment(
      """
          Under behalf of which user to connect on the DBMS server
          Value can't be empty or blank""")
  private final String username;

  @NotNull
  @Size(max = 128)
  @Required
  @Comment("Password of the user (optional but highly recommended)")
  private final String password;

  public DbmsCredentialsPropertiesImpl() {
    this.username = "username";
    this.password = "password";
  }

  /** Testing purposes only. */
  public DbmsCredentialsPropertiesImpl(@Nullable String username, @Nullable String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public @org.jetbrains.annotations.NotNull String getUsername() {
    return Objects.requireNonNull(username);
  }

  @Override
  public @org.jetbrains.annotations.NotNull String getPassword() {
    return Objects.requireNonNull(password);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DbmsCredentialsPropertiesImpl that = (DbmsCredentialsPropertiesImpl) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  @Override
  public @org.jetbrains.annotations.NotNull String toString() {
    return new StringJoiner(", ", DbmsCredentialsPropertiesImpl.class.getSimpleName() + "[", "]")
        .add("username='" + username + "'")
        .toString();
  }
}
