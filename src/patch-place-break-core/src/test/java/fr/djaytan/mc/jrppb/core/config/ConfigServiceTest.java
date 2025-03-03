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
package fr.djaytan.mc.jrppb.core.config;

import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.NOMINAL_CONFIG_NAME;
import static fr.djaytan.mc.jrppb.core.config.properties.AnotherConfigProperties.ANOTHER_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.NominalConfigProperties.NOMINAL_CONFIG_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.properties.AnotherConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.ConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.NominalConfigProperties;
import fr.djaytan.mc.jrppb.core.config.repository.ConfigRepository;
import fr.djaytan.mc.jrppb.core.config.repository.SimpleConfigRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

final class ConfigServiceTest {

  private static final ConfigName ANOTHER_CONFIG_NAME = new ConfigName("anotherConfig");

  private final ConfigRepository configRepository = new SimpleConfigRepository();
  private final ConfigService configService = new ConfigService(configRepository);

  @Test
  void withoutAnyConfig() {
    assertMissingConfig(NOMINAL_CONFIG_NAME, NominalConfigProperties.class);
    assertMissingConfig(ANOTHER_CONFIG_NAME, AnotherConfigProperties.class);
  }

  @Test
  void whenTryingToCreateNonExistingNominalConfig_shallCreateIt() {
    configService.createIfNotExists(NOMINAL_CONFIG_NAME, NOMINAL_CONFIG_PROPERTIES);

    assertConfigExistence(NOMINAL_CONFIG_NAME, NOMINAL_CONFIG_PROPERTIES);
    assertMissingConfig(ANOTHER_CONFIG_NAME, AnotherConfigProperties.class);
  }

  @Test
  void whenTryingToCreateAlreadyExistingNominalConfig_shallDoNothing() {
    configService.createIfNotExists(NOMINAL_CONFIG_NAME, NOMINAL_CONFIG_PROPERTIES);
    configService.createIfNotExists(NOMINAL_CONFIG_NAME, new NominalConfigProperties("another", 1));

    assertConfigExistence(NOMINAL_CONFIG_NAME, NOMINAL_CONFIG_PROPERTIES);
    assertMissingConfig(ANOTHER_CONFIG_NAME, AnotherConfigProperties.class);
  }

  @Test
  void whenTryingToCreateMultipleNonExistingConfigs_shallCreateThem() {
    configService.createIfNotExists(NOMINAL_CONFIG_NAME, NOMINAL_CONFIG_PROPERTIES);
    configService.createIfNotExists(ANOTHER_CONFIG_NAME, ANOTHER_CONFIG_PROPERTIES);

    assertConfigExistence(NOMINAL_CONFIG_NAME, NOMINAL_CONFIG_PROPERTIES);
    assertConfigExistence(ANOTHER_CONFIG_NAME, ANOTHER_CONFIG_PROPERTIES);
  }

  private void assertMissingConfig(
      @NotNull ConfigName configName,
      @NotNull Class<? extends ConfigProperties> configPropertiesType) {
    assertThatThrownBy(() -> configService.load(configName, configPropertiesType))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("The config '%s' can't be found whereas it shall exist", configName.value())
        .hasNoCause();
    assertThat(configRepository.exists(configName)).isFalse();
  }

  private void assertConfigExistence(
      @NotNull ConfigName configName, @NotNull ConfigProperties expectedConfigProperties) {
    assertThat(configService.load(configName, expectedConfigProperties.getClass()))
        .isEqualTo(expectedConfigProperties);
    assertThat(configRepository.exists(configName)).isTrue();
  }
}
