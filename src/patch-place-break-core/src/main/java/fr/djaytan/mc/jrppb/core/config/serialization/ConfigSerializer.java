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
package fr.djaytan.mc.jrppb.core.config.serialization;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.util.NamingSchemes;

public final class ConfigSerializer {

  private static final String CONFIG_HEADER =
      """
               JobsReborn-PatchPlaceBreak
       A patch place-break extension for JobsReborn
                      (by Djaytan)

       This config file use HOCON format
       Specifications are here: https://github.com/lightbend/config/blob/main/HOCON.md
      """;

  private ConfigSerializer() {
    // Static class
  }

  /**
   * Serializes the specified config properties to HOCON format.
   *
   * @param configProperties The config properties to serialize.
   * @return The serialized form of the initially provided config properties.
   * @throws ConfigSerializationException If something wrong happened during serialization.
   */
  public static @NotNull String serialize(@NotNull Object configProperties)
      throws ConfigSerializationException {
    try {
      var configLoaderBuilder = configLoaderBuilder();

      // Weird... Because of Configurate itself
      ConfigurationNode configNode =
          configLoaderBuilder.build().createNode(node -> node.set(configProperties));

      return configLoaderBuilder.buildAndSaveString(configNode);
    } catch (ConfigurateException e) {
      throw new ConfigSerializationException(
          "Fail to serialize the following config properties: " + configProperties, e);
    }
  }

  /**
   * Deserializes the provided input to the specified target type if possible, otherwise fails.
   *
   * @param configInput The input to deserialize.
   * @param configType The target type for the deserialization.
   * @return The deserialized form of the input corresponding to the specified target type.
   * @param <T> The deserialization target type.
   * @throws ConfigSerializationException If something wrong happened during deserialization.
   */
  public static <T> @NotNull T deserialize(
      @NotNull String configInput, @NotNull Class<T> configType)
      throws ConfigSerializationException {
    try {
      T deserialized = configLoaderBuilder().buildAndLoadString(configInput).get(configType);

      if (deserialized == null) {
        throw new ConfigSerializationException(
            "Unexpectedly deserialized the following input to null:\n%s".formatted(configInput));
      }

      return deserialized;
    } catch (ConfigurateException e) {
      throw new ConfigSerializationException(
          "Fail to deserialize config properties of type '%s' from the following config input:\n%s"
              .formatted(configType.getName(), configInput),
          e);
    }
  }

  private static @NotNull HoconConfigurationLoader.Builder configLoaderBuilder() {
    var configLoaderBuilder =
        HoconConfigurationLoader.builder().emitComments(true).prettyPrinting(true);

    // Header
    configLoaderBuilder =
        configLoaderBuilder
            .headerMode(HeaderMode.PRESET)
            .defaultOptions(opts -> opts.header(CONFIG_HEADER));

    // Object mapper
    configLoaderBuilder =
        configLoaderBuilder.defaultOptions(
            opts ->
                opts.serializers(
                    builder -> builder.registerAnnotatedObjects(customObjectMapperFactory())));

    return configLoaderBuilder;
  }

  private static @NotNull ObjectMapper.Factory customObjectMapperFactory() {
    return ObjectMapper.factoryBuilder().defaultNamingScheme(NamingSchemes.CAMEL_CASE).build();
  }
}
