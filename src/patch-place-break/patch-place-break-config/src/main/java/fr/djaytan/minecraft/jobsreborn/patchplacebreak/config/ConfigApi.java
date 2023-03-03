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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DataSourceValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;

/** API permitting to retrieve config properties. */
@Singleton
public final class ConfigApi {

  private static final String DATA_SOURCE_CONFIG_FILE_NAME = "dataSource.conf";

  private final ConfigManager configManager;

  @Inject
  public ConfigApi(ConfigManager configManager) {
    this.configManager = configManager;
  }

  /**
   * Retrieves data source related properties.
   *
   * @return The data source related properties.
   */
  public @NonNull DataSourceProperties getDataSourceProperties() {
    configManager.createDefaultIfNotExists(
        DATA_SOURCE_CONFIG_FILE_NAME, DataSourceValidatingProperties.ofDefault());
    return configManager.readAndValidate(
        DATA_SOURCE_CONFIG_FILE_NAME, DataSourceValidatingProperties.class);
  }
}
