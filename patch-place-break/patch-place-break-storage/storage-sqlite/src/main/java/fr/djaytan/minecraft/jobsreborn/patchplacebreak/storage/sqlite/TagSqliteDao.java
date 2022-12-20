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

import static fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.SqlDataSource.SQL_TABLE_NAME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.DaoException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.TagDao;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.SqlDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.StorageException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite.serializer.LocalDateTimeStringSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sqlite.serializer.UUIDStringSerializer;
import lombok.NonNull;

@Singleton
public class TagSqliteDao implements TagDao {

  private final BooleanIntegerSerializer booleanIntegerSerializer;
  private final SqlDataSource sqlDataSource;
  private final LocalDateTimeStringSerializer localDateTimeStringSerializer;
  private final Logger logger;
  private final UUIDStringSerializer uuidStringSerializer;

  @Inject
  public TagSqliteDao(BooleanIntegerSerializer booleanIntegerSerializer,
      SqlDataSource sqlDataSource, LocalDateTimeStringSerializer localDateTimeStringSerializer,
      Logger logger, UUIDStringSerializer uuidStringSerializer) {
    this.booleanIntegerSerializer = booleanIntegerSerializer;
    this.sqlDataSource = sqlDataSource;
    this.localDateTimeStringSerializer = localDateTimeStringSerializer;
    this.logger = logger;
    this.uuidStringSerializer = uuidStringSerializer;
  }

  @Override
  public void put(@NonNull Tag tag) {
    try (Connection connection = sqlDataSource.getConnection()) {
      String sqlInsert =
          String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?)", SQL_TABLE_NAME);

      try (PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)) {
        connection.setAutoCommit(false);

        deleteTag(connection, tag.getTagLocation());

        insertStmt.setString(1, uuidStringSerializer.serialize(tag.getUuid()));
        insertStmt.setString(2,
            localDateTimeStringSerializer.serialize(tag.getInitLocalDateTime()));
        insertStmt.setInt(3, booleanIntegerSerializer.serialize(tag.isEphemeral()));
        insertStmt.setString(4, tag.getTagLocation().getWorldName());
        insertStmt.setDouble(5, tag.getTagLocation().getX());
        insertStmt.setDouble(6, tag.getTagLocation().getY());
        insertStmt.setDouble(7, tag.getTagLocation().getZ());
        insertStmt.executeUpdate();
      } catch (SQLException e) {
        throw DaoException.put(tag, e);
      }
      connection.commit();
    } catch (SQLException e) {
      throw StorageException.databaseConnectionReleasing(e);
    }
  }

  @Override
  public @NonNull Optional<Tag> findByLocation(@NonNull TagLocation tagLocation) {
    try (Connection connection = sqlDataSource.getConnection()) {
      String sqlQuery = String
          .format("SELECT * FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
              + " location_z = ?", SQL_TABLE_NAME);

      try (PreparedStatement queryStmt = connection.prepareStatement(sqlQuery)) {
        queryStmt.setString(1, tagLocation.getWorldName());
        queryStmt.setDouble(2, tagLocation.getX());
        queryStmt.setDouble(3, tagLocation.getY());
        queryStmt.setDouble(4, tagLocation.getZ());

        ResultSet rs = queryStmt.executeQuery();
        if (rs.getFetchSize() > 1) {
          logger.atWarn()
              .log("Multiple tags detected for a same location, selecting the first one.");
        }

        if (rs.next()) {
          UUID tagUuid = uuidStringSerializer.deserialize(rs.getString("tag_uuid"));
          LocalDateTime initLocalDateTime =
              localDateTimeStringSerializer.deserialize(rs.getString("init_timestamp"));
          boolean isEphemeral = booleanIntegerSerializer.deserialize(rs.getInt("is_ephemeral"));
          String worldName = rs.getString("world_name");
          double x = rs.getDouble("location_x");
          double y = rs.getDouble("location_y");
          double z = rs.getDouble("location_z");

          TagLocation location = TagLocation.of(worldName, x, y, z);
          Tag tag = Tag.of(tagUuid, initLocalDateTime, isEphemeral, location);
          return Optional.of(tag);
        }
        return Optional.empty();
      } catch (SQLException e) {
        throw DaoException.fetch(tagLocation, e);
      }
    } catch (SQLException e) {
      throw StorageException.databaseConnectionReleasing(e);
    }
  }

  @Override
  public void delete(@NonNull TagLocation tagLocation) {
    try (Connection connection = sqlDataSource.getConnection()) {
      deleteTag(connection, tagLocation);
    } catch (SQLException e) {
      throw StorageException.databaseConnectionReleasing(e);
    }
  }

  private void deleteTag(@NonNull Connection connection, @NonNull TagLocation tagLocation) {
    String sqlDelete = String
        .format("DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
            + " location_z = ?", SQL_TABLE_NAME);

    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
      deleteStmt.setString(1, tagLocation.getWorldName());
      deleteStmt.setDouble(2, tagLocation.getX());
      deleteStmt.setDouble(3, tagLocation.getY());
      deleteStmt.setDouble(4, tagLocation.getZ());
      deleteStmt.executeUpdate();
    } catch (SQLException e) {
      throw DaoException.delete(tagLocation, e);
    }
  }
}
