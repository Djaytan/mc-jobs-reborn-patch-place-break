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
  public boolean isDatabaseExists(@NonNull Connection connection) throws SQLException {
    String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, dataSourceProperties.getDatabaseName());
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    }
  }

  @Override
  public void createDatabase(@NonNull Connection connection) throws SQLException {
    String sql = "CREATE DATABASE %s";

    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format(sql, dataSourceProperties.getDatabaseName()));
    }
  }

  @Override
  public boolean isTableExists(@NonNull Connection connection) throws SQLException {
    String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, dataSourceProperties.getTableName());
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    }
  }
}
