package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.inject.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakCoreException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql.TagMysqlDataDefiner;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.TagSqlDataDefiner;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sqlite.TagSqliteDataDefiner;

public class TagSqlDataDefinerProvider implements Provider<TagSqlDataDefiner> {

  private final DataSourceProperties dataSourceProperties;
  private final TagMysqlDataDefiner tagMysqlDataDefiner;
  private final TagSqliteDataDefiner tagSqliteDataDefiner;

  @Inject
  TagSqlDataDefinerProvider(DataSourceProperties dataSourceProperties,
      TagMysqlDataDefiner tagMysqlDataDefiner, TagSqliteDataDefiner tagSqliteDataDefiner) {
    this.dataSourceProperties = dataSourceProperties;
    this.tagMysqlDataDefiner = tagMysqlDataDefiner;
    this.tagSqliteDataDefiner = tagSqliteDataDefiner;
  }

  @Override
  public TagSqlDataDefiner get() {
    DataSourceType dataSourceType = dataSourceProperties.getType();

    switch (dataSourceType) {
      case MYSQL: {
        return tagMysqlDataDefiner;
      }
      case SQLITE: {
        return tagSqliteDataDefiner;
      }
      default: {
        throw PatchPlaceBreakCoreException.unrecognisedDataSourceType(dataSourceType);
      }
    }
  }
}
