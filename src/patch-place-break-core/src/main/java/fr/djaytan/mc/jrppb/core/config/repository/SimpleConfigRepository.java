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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public final class SimpleConfigRepository implements ConfigRepository {

  private final Map<ConfigName, String> configs = new HashMap<>();

  @Override
  public void create(@NotNull ConfigName configName, @NotNull String content) {
    if (exists(configName)) {
      throw new IllegalStateException(
          "Failed to create config named '%s' because it already exists"
              .formatted(configName.value()));
    }
    configs.put(configName, content);
  }

  @Override
  public boolean exists(@NotNull ConfigName configName) {
    return configs.containsKey(configName);
  }

  @Override
  public @NotNull Optional<String> findByName(@NotNull ConfigName configName) {
    return Optional.ofNullable(configs.get(configName));
  }

  public boolean isEmpty() {
    return configs.isEmpty();
  }
}
