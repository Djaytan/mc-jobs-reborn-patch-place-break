package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.inmemory.TagInMemoryRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.access.TagSqlRepository;
import javax.inject.Inject;
import javax.inject.Provider;
import lombok.NonNull;

public class TagRepositoryProvider implements Provider<TagRepository> {

  private final DataSourceProperties dataSourceProperties;
  private final TagInMemoryRepository tagInMemoryRepository;
  private final TagSqlRepository tagSqlRepository;

  @Inject
  public TagRepositoryProvider(
      DataSourceProperties dataSourceProperties,
      TagInMemoryRepository tagInMemoryRepository,
      TagSqlRepository tagSqlRepository) {
    this.dataSourceProperties = dataSourceProperties;
    this.tagInMemoryRepository = tagInMemoryRepository;
    this.tagSqlRepository = tagSqlRepository;
  }

  @Override
  public @NonNull TagRepository get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case IN_MEMORY:
        {
          return tagInMemoryRepository;
        }
      case MYSQL:
      case SQLITE:
        {
          return tagSqlRepository;
        }
      default:
        {
          throw PatchPlaceBreakException.unrecognisedDataSourceType(dataSourceType);
        }
    }
  }
}
