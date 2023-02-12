/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.util.NamingSchemes;

import lombok.NonNull;

/**
 * Factory of config loader.
 *
 * <p>The method {@link #createLoader(Path)} is the one to use in order to create a loader.
 */
final class ConfigLoaderFactory {

  private static final String CONFIG_HEADER_RESOURCE = "configHeader.txt";

  private ConfigLoaderFactory() {
    // Static class
  }

  /**
   * Creates a configuration loader with the specified config file.
   *
   * @param configFile The config file path from which to create the loader.
   * @return A configuration loader for the specified config file.
   * @throws IOException If something prevent the configuration loader creation.
   */
  static @NonNull ConfigurationLoader<CommentedConfigurationNode> createLoader(
      @NonNull Path configFile) throws IOException {
    ObjectMapper.Factory customFactory =
        ObjectMapper.factoryBuilder().defaultNamingScheme(NamingSchemes.CAMEL_CASE).build();

    String configHeader = createConfigHeader();

    return HoconConfigurationLoader.builder().path(configFile).prettyPrinting(true)
        .emitComments(true).emitJsonCompatible(false).headerMode(HeaderMode.PRESET)
        .defaultOptions(
            opts -> opts.serializers(builder -> builder.registerAnnotatedObjects(customFactory))
                .header(configHeader))
        .build();
  }

  private static @NonNull String createConfigHeader() throws IOException {
    return IOUtils.resourceToString(CONFIG_HEADER_RESOURCE, StandardCharsets.UTF_8,
        ConfigLoaderFactory.class.getClassLoader());
  }
}
