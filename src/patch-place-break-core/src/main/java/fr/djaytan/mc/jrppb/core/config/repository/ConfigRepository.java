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
package fr.djaytan.mc.jrppb.core.config.repository;

import fr.djaytan.mc.jrppb.core.config.ConfigName;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/** Represents a repository where configs are stored. */
public interface ConfigRepository {

  /**
   * Creates a new config.
   *
   * <p>It is recommended to first check for config existence with {@link #exists(ConfigName)}
   * before calling this method.
   *
   * @param configName The name of the config to create.
   * @param content The content of the config to create.
   * @throws IllegalStateException If the config already exists.
   */
  void create(@NotNull ConfigName configName, @NotNull String content);

  /**
   * Checks config existence.
   *
   * @param configName The name of the config to check existence.
   * @return {@code true} if the config exists, {@code false} otherwise.
   */
  boolean exists(@NotNull ConfigName configName);

  /**
   * Searches the config matching the provided name if it exists.
   *
   * @param configName The name of the config to search.
   * @return The matching config if it exists, otherwise nothing.
   */
  @NotNull
  Optional<String> findByName(@NotNull ConfigName configName);
}
