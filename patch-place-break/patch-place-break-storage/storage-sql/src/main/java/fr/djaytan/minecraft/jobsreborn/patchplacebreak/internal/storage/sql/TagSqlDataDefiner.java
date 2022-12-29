package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import lombok.NonNull;

public abstract class TagSqlDataDefiner {

  protected final DataSourceProperties dataSourceProperties;

  protected TagSqlDataDefiner(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  public abstract boolean isDatabaseExists(@NonNull Connection connection) throws SQLException;

  public abstract void createDatabase(@NonNull Connection connection) throws SQLException;

  public void useDatabase(@NonNull Connection connection) throws SQLException {
    String sql = "USE %s";

    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format(sql, dataSourceProperties.getDatabaseName()));
    }
  }

  public abstract boolean isTableExists(@NonNull Connection connection) throws SQLException;

  public void createTable(@NonNull Connection connection) throws SQLException {
    String sql = "CREATE TABLE %s (\n" + "  tag_uuid TEXT PRIMARY KEY NOT NULL,\n"
        + "  init_timestamp TEXT NOT NULL,\n" + "  is_ephemeral INTEGER NOT NULL,\n"
        + "  world_name TEXT NOT NULL,\n" + "  location_x REAL NOT NULL,\n"
        + "  location_y REAL NOT NULL,\n" + "  location_z REAL NOT NULL\n" + ");";

    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format(sql, dataSourceProperties.getTableName()));
    }
  }
}
