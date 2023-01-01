package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.sql.TagSqlDataDefiner;
import lombok.NonNull;

@Singleton
public class TagMysqlDataDefiner extends TagSqlDataDefiner {

  @Inject
  TagMysqlDataDefiner(DataSourceProperties dataSourceProperties) {
    super(dataSourceProperties);
  }

  @Override
  public boolean isTableExists(@NonNull Connection connection) throws SQLException {
    String sql = "SELECT table_name " + "FROM information_schema.tables "
        + "WHERE table_schema = ? " + "AND table_type = 'BASE TABLE' " + "AND table_name = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, dataSourceProperties.getDatabaseName());
      preparedStatement.setString(2, dataSourceProperties.getTableName());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return resultSet.next();
      }
    }
  }

  public void createTable(@NonNull Connection connection) throws SQLException {
    String sql = "CREATE TABLE %s (\n" + "  tag_uuid CHAR(36) PRIMARY KEY NOT NULL,\n"
        + "  init_timestamp VARCHAR(64) NOT NULL,\n" + "  is_ephemeral INTEGER NOT NULL,\n"
        + "  world_name VARCHAR(128) NOT NULL,\n" + "  location_x DOUBLE NOT NULL,\n"
        + "  location_y DOUBLE NOT NULL,\n" + "  location_z DOUBLE NOT NULL\n" + ");";

    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format(sql, dataSourceProperties.getTableName()));
    }
  }
}
