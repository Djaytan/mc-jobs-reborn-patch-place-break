package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import lombok.NonNull;

public abstract class TagSqlDataDefiner {

  protected final DataSourceProperties dataSourceProperties;

  protected TagSqlDataDefiner(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  public abstract boolean isTableExists(@NonNull Connection connection) throws SQLException;

  public abstract void createTable(@NonNull Connection connection) throws SQLException;
}
