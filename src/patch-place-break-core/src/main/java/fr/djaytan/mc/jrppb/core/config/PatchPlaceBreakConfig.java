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

import fr.djaytan.mc.jrppb.core.config.properties_v2.DataSourceConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties_v2.RestrictedBlocksConfigProperties;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class PatchPlaceBreakConfig {

  public static final ConfigName DATA_SOURCE_CONFIG_NAME = new ConfigName("dataSource");
  public static final ConfigName RESTRICTED_BLOCKS_CONFIG_NAME = new ConfigName("restrictedBlocks");

  private final ConfigService configService;

  @Inject
  public PatchPlaceBreakConfig(@NotNull ConfigService configService) {
    this.configService = configService;
  }

  public @NotNull DataSourceConfigProperties dataSourceConfigProperties() {
    configService.createIfNotExists(DATA_SOURCE_CONFIG_NAME, DataSourceConfigProperties.DEFAULT);
    return configService.load(DATA_SOURCE_CONFIG_NAME, DataSourceConfigProperties.class);
  }

  public @NotNull RestrictedBlocksConfigProperties restrictedBlocksConfigProperties() {
    configService.createIfNotExists(
        RESTRICTED_BLOCKS_CONFIG_NAME, RestrictedBlocksConfigProperties.DEFAULT);
    return configService.load(
        RESTRICTED_BLOCKS_CONFIG_NAME, RestrictedBlocksConfigProperties.class);
  }
}
