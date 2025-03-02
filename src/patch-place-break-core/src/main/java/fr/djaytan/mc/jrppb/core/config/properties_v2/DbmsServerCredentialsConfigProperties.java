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

import fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerCredentialsProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record DbmsServerCredentialsConfigProperties(
    @Required @Comment(USERNAME_COMMENT) @NotNull String username,
    @Required @Comment(PASSWORD_COMMENT) @NotNull String password)
    implements ConfigProperties {

  private static final String USERNAME_COMMENT =
      """
      Under behalf of which user to connect on the DBMS server
      Value can't be empty or blank""";

  private static final String PASSWORD_COMMENT =
      "Password of the user (optional but highly recommended)";

  public static @NotNull DbmsServerCredentialsConfigProperties fromModel(
      @NotNull DbmsServerCredentialsProperties model) {
    return new DbmsServerCredentialsConfigProperties(model.username(), model.password());
  }

  public @NotNull DbmsServerCredentialsProperties toModel() {
    return new DbmsServerCredentialsProperties(username, password);
  }
}
