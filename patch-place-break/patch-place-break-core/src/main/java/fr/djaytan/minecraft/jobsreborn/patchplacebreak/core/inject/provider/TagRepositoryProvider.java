package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakStandaloneException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.inmemory.TagInMemoryRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.TagSqlRepository;

public class TagRepositoryProvider implements Provider<TagRepository> {

  private final DataSourceProperties dataSourceProperties;
  private final TagInMemoryRepository tagInMemoryRepository;
  private final TagSqlRepository tagSqlRepository;

  @Inject
  TagRepositoryProvider(DataSourceProperties dataSourceProperties,
      TagInMemoryRepository tagInMemoryRepository, TagSqlRepository tagSqlRepository) {
    this.dataSourceProperties = dataSourceProperties;
    this.tagInMemoryRepository = tagInMemoryRepository;
    this.tagSqlRepository = tagSqlRepository;
  }

  @Override
  public TagRepository get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case IN_MEMORY: {
        return tagInMemoryRepository;
      }
      case MYSQL:
      case SQLITE: {
        return tagSqlRepository;
      }
      default: {
        throw PatchPlaceBreakStandaloneException.unrecognisedDataSourceType(dataSourceType);
      }
    }
  }
}
