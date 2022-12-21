/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite;

import static fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlDataSourceInitializer.SQL_TABLE_NAME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.TagSqlDao;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.LocalDateTimeStringSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.UUIDStringSerializer;
import lombok.NonNull;

@Singleton
public class TagSqliteDao extends TagSqlDao {

  private final String createTableSqlScript;

  public TagSqliteDao(BooleanIntegerSerializer booleanIntegerSerializer,
      LocalDateTimeStringSerializer localDateTimeStringSerializer, Logger logger,
      UUIDStringSerializer uuidStringSerializer,
      @Named("createTableSqlScript") String createTableSqlScript) {
    super(booleanIntegerSerializer, localDateTimeStringSerializer, logger, uuidStringSerializer);
    this.createTableSqlScript = createTableSqlScript;
  }

  @Override
  public boolean isTableExists(@NonNull Connection connection) throws SQLException {
    String sql = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, SQL_TABLE_NAME);
      ResultSet rs = preparedStatement.executeQuery();
      return rs.next();
    }
  }

  @Override
  public void createTable(@NonNull Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.addBatch(createTableSqlScript);
      statement.executeBatch();
      connection.commit();
    }
  }
}
