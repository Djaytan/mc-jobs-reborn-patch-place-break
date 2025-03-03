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

import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.NOMINAL_CONFIG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.ConfigName;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

abstract sealed class ConfigRepositoryTest
    permits FileSystemConfigRepositoryTest, SimpleConfigRepositoryTest {

  private static final ConfigName CONFIG_NAME_1 = NOMINAL_CONFIG_NAME;
  private static final ConfigName CONFIG_NAME_2 = new ConfigName("anotherName");

  protected static final String NOMINAL_CONFIG_CONTENT = "nominal content";

  protected abstract @NotNull ConfigRepository configRepository();

  @Test
  final void withoutAnyConfig() {
    assertMissingConfig(CONFIG_NAME_1);
    assertMissingConfig(CONFIG_NAME_2);
    assertEmptyDataSource();
  }

  protected abstract void assertEmptyDataSource();

  @Test
  final void whenCreatingOneConfig() {
    configRepository().create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT);

    assertConfigExistence(CONFIG_NAME_1);
    assertMissingConfig(CONFIG_NAME_2);
  }

  @Test
  final void whenCreatingMultipleConfigs() {
    configRepository().create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT);
    configRepository().create(CONFIG_NAME_2, NOMINAL_CONFIG_CONTENT);

    assertConfigExistence(CONFIG_NAME_1);
    assertConfigExistence(CONFIG_NAME_2);
  }

  @Test
  final void whenCreatingAlreadyExistingConfig() {
    configRepository().create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT);

    assertThatThrownBy(() -> configRepository().create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage(
            "Failed to create config named '%s' because it already exists", CONFIG_NAME_1.value())
        .hasNoCause();
  }

  private void assertMissingConfig(@NotNull ConfigName configName) {
    assertThat(configRepository().exists(configName)).isFalse();
    assertThat(configRepository().findByName(configName)).isEmpty();
    assertMissingConfigSideEffect(configName);
  }

  protected abstract void assertMissingConfigSideEffect(@NotNull ConfigName configName);

  private void assertConfigExistence(@NotNull ConfigName configName) {
    assertThat(configRepository().exists(configName)).isTrue();
    assertThat(configRepository().findByName(configName))
        .isPresent()
        .contains(NOMINAL_CONFIG_CONTENT);
    assertConfigExistenceSideEffect(configName);
  }

  protected abstract void assertConfigExistenceSideEffect(@NotNull ConfigName configName);
}
