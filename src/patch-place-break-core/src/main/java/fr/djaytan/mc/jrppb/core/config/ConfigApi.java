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

import fr.djaytan.mc.jrppb.api.properties.RestrictedBlocksProperties;
import fr.djaytan.mc.jrppb.core.config.properties.DataSourcePropertiesImpl;
import fr.djaytan.mc.jrppb.core.config.properties.RestrictedBlocksPropertiesImpl;
import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceProperties;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

/** API permitting to retrieve config properties. */
@Singleton
public final class ConfigApi {

  private static final String DATA_SOURCE_CONFIG_FILE_NAME = "dataSource.conf";
  private static final String RESTRICTED_BLOCKS_CONFIG_FILE_NAME = "restrictedBlocks.conf";

  private final ConfigManager configManager;

  @Inject
  ConfigApi(@NotNull ConfigManager configManager) {
    this.configManager = configManager;
  }

  /**
   * Retrieves data source related properties.
   *
   * @return The data source related properties.
   */
  public @NotNull DataSourceProperties getDataSourceProperties() {
    configManager.createDefaultIfNotExists(
        DATA_SOURCE_CONFIG_FILE_NAME, new DataSourcePropertiesImpl());
    return configManager.readAndValidate(
        DATA_SOURCE_CONFIG_FILE_NAME, DataSourcePropertiesImpl.class);
  }

  public @NotNull RestrictedBlocksProperties getRestrictedBlocksProperties() {
    configManager.createDefaultIfNotExists(
        RESTRICTED_BLOCKS_CONFIG_FILE_NAME, new RestrictedBlocksPropertiesImpl());
    return configManager.readAndValidate(
        RESTRICTED_BLOCKS_CONFIG_FILE_NAME, RestrictedBlocksPropertiesImpl.class);
  }
}
