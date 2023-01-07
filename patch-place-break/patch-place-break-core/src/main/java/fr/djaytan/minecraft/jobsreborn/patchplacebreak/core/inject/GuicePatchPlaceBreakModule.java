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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject;

import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.Config;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.ConfigManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.serializer.ConfigSerializerCollection;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.ConnectionPoolProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.DataSourceProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.SqlDataSourceInitializerProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.TagRepositoryProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider.TagSqlDataDefinerProvider;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.PatchPlaceBreakDefault;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.DataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.ConnectionPool;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.TagSqlDataDefiner;
import lombok.NonNull;

public class GuicePatchPlaceBreakModule extends AbstractModule {

  private final ClassLoader classLoader;
  private final Path dataFolder;

  public GuicePatchPlaceBreakModule(@NonNull ClassLoader classLoader, @NonNull Path dataFolder) {
    this.classLoader = classLoader;
    this.dataFolder = dataFolder;
  }

  @Override
  protected void configure() {
    bind(ClassLoader.class).toInstance(classLoader);

    bind(PatchPlaceBreakApi.class).to(PatchPlaceBreakDefault.class);

    bind(ConnectionPool.class).toProvider(ConnectionPoolProvider.class);
    bind(DataSource.class).toProvider(DataSourceProvider.class);
    bind(SqlDataSourceInitializer.class).toProvider(SqlDataSourceInitializerProvider.class);
    bind(TagRepository.class).toProvider(TagRepositoryProvider.class);
    bind(TagSqlDataDefiner.class).toProvider(TagSqlDataDefinerProvider.class);
  }

  @Provides
  @Named("dataFolder")
  @Singleton
  public Path provideDataFolder() {
    return dataFolder;
  }

  @Provides
  @Named("configFile")
  @Singleton
  public Path provideConfigFile(@Named("dataFolder") Path dataFolder) {
    return dataFolder.resolve(ConfigManager.CONFIG_FILE_NAME);
  }

  @Provides
  @Singleton
  public ConfigurationLoader<CommentedConfigurationNode> provideConfigurationLoader(
      @Named("configFile") Path configFile, ConfigSerializerCollection configSerializerCollection) {
    return YamlConfigurationLoader.builder()
        .defaultOptions(configurationOptions -> configurationOptions
            .serializers(builder -> builder.registerAll(configSerializerCollection.collection())))
        .path(configFile).build();
  }

  @Provides
  @Singleton
  public Config provideConfig(ConfigManager configManager) {
    configManager.createDefaultIfNotExists();
    return configManager.readConfig();
  }

  @Provides
  @Singleton
  public DataSourceProperties provideDataSourceProperties(Config config) {
    return config.getDataSource();
  }
}
