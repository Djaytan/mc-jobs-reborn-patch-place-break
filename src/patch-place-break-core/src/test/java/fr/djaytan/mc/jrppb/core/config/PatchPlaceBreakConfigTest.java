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

import static fr.djaytan.mc.jrppb.core.config.PatchPlaceBreakConfig.DATA_SOURCE_CONFIG_NAME;
import static fr.djaytan.mc.jrppb.core.config.PatchPlaceBreakConfig.RESTRICTED_BLOCKS_CONFIG_NAME;
import static fr.djaytan.mc.jrppb.core.config.properties.DataSourceConfigPropertiesTestDataSet.NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.RestrictedBlocksConfigPropertiesTestDataSet.NOMINAL_RESTRICTED_BLOCKS_CONFIG_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import fr.djaytan.mc.jrppb.core.config.properties.ConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.DataSourceConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.RestrictedBlocksConfigProperties;
import fr.djaytan.mc.jrppb.core.config.repository.SimpleConfigRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class PatchPlaceBreakConfigTest {

  private final ConfigService configService = new ConfigService(new SimpleConfigRepository());
  private final PatchPlaceBreakConfig patchPlaceBreakConfig =
      new PatchPlaceBreakConfig(configService);

  @Test
  void whenNothingHasBeenDoneYet_thenNoConfigShallExist() {
    assertMissingConfig(DATA_SOURCE_CONFIG_NAME, DataSourceConfigProperties.class);
    assertMissingConfig(RESTRICTED_BLOCKS_CONFIG_NAME, RestrictedBlocksConfigProperties.class);
  }

  @Nested
  class WhenDataSourceConfigPropertiesRequested {

    @Test
    void whileMissing_shallCreateDefaultProperties() {
      assertThat(patchPlaceBreakConfig.dataSourceConfigProperties())
          .isEqualTo(DataSourceConfigProperties.DEFAULT);
      assertConfigExistence(DATA_SOURCE_CONFIG_NAME, DataSourceConfigProperties.DEFAULT);
      assertMissingConfig(RESTRICTED_BLOCKS_CONFIG_NAME, RestrictedBlocksConfigProperties.class);
    }

    @Test
    void whileAlreadyExists_shallRetrieveLoadedProperties() {
      configService.createIfNotExists(
          DATA_SOURCE_CONFIG_NAME, NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES);

      assertThat(patchPlaceBreakConfig.dataSourceConfigProperties())
          .isEqualTo(NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES);
      assertConfigExistence(DATA_SOURCE_CONFIG_NAME, NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES);
      assertMissingConfig(RESTRICTED_BLOCKS_CONFIG_NAME, RestrictedBlocksConfigProperties.class);
    }
  }

  @Nested
  class WhenRestrictedBlocksConfigPropertiesRequested {

    @Test
    void whileMissing_shallCreateDefaultProperties() {
      assertThat(patchPlaceBreakConfig.restrictedBlocksConfigProperties())
          .isEqualTo(RestrictedBlocksConfigProperties.DEFAULT);
      assertMissingConfig(DATA_SOURCE_CONFIG_NAME, DataSourceConfigProperties.class);
      assertConfigExistence(
          RESTRICTED_BLOCKS_CONFIG_NAME, RestrictedBlocksConfigProperties.DEFAULT);
    }

    @Test
    void whileAlreadyExists_shallRetrieveLoadedProperties() {
      configService.createIfNotExists(
          RESTRICTED_BLOCKS_CONFIG_NAME, NOMINAL_RESTRICTED_BLOCKS_CONFIG_PROPERTIES);

      assertThat(patchPlaceBreakConfig.restrictedBlocksConfigProperties())
          .isEqualTo(NOMINAL_RESTRICTED_BLOCKS_CONFIG_PROPERTIES);
      assertMissingConfig(DATA_SOURCE_CONFIG_NAME, DataSourceConfigProperties.class);
      assertConfigExistence(
          RESTRICTED_BLOCKS_CONFIG_NAME, NOMINAL_RESTRICTED_BLOCKS_CONFIG_PROPERTIES);
    }
  }

  private void assertMissingConfig(
      @NotNull ConfigName configName,
      @NotNull Class<? extends ConfigProperties> configPropertiesType) {
    assertThat(catchThrowable(() -> configService.load(configName, configPropertiesType)))
        .isNotNull();
  }

  private void assertConfigExistence(
      @NotNull ConfigName configName, @NotNull ConfigProperties expectedConfigProperties) {
    assertThat(configService.load(configName, expectedConfigProperties.getClass()))
        .isEqualTo(expectedConfigProperties);
  }
}
