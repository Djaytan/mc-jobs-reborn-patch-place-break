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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import com.google.inject.name.Named;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.TagDao;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.SqlDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite.serializer.LocalDateTimeStringSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite.serializer.UUIDStringSerializer;

@Singleton
public class TagSqliteDao implements TagDao {

  private final BooleanIntegerSerializer booleanIntegerSerializer;
  private final SqlDataSource sqlDataSource;
  private final LocalDateTimeStringSerializer localDateTimeStringSerializer;
  private final Logger logger;
  private final UUIDStringSerializer uuidStringSerializer;

  @Inject
  public TagSqliteDao(@NotNull BooleanIntegerSerializer booleanIntegerSerializer,
      @NotNull SqlDataSource sqlDataSource,
      @NotNull LocalDateTimeStringSerializer localDateTimeStringSerializer,
      @NotNull @Named("PatchPlaceBreakLogger") Logger logger,
      @NotNull UUIDStringSerializer uuidStringSerializer) {
    this.booleanIntegerSerializer = booleanIntegerSerializer;
    this.sqlDataSource = sqlDataSource;
    this.localDateTimeStringSerializer = localDateTimeStringSerializer;
    this.logger = logger;
    this.uuidStringSerializer = uuidStringSerializer;
  }

  @Override
  public void put(@NotNull Tag tag) {
    try (Connection connection = sqlDataSource.getConnection()) {
      connection.setAutoCommit(false);

      deleteTag(connection, tag.getTagLocation());

      String sqlInsert = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?)",
          SqlDataSource.SQL_TABLE_NAME);

      try (PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)) {
        insertStmt.setString(1, uuidStringSerializer.serialize(tag.getUuid()));
        insertStmt.setString(2,
            localDateTimeStringSerializer.serialize(tag.getInitLocalDateTime()));
        insertStmt.setInt(3, booleanIntegerSerializer.serialize(tag.isEphemeral()));
        insertStmt.setString(4, tag.getTagLocation().getWorldName());
        insertStmt.setDouble(5, tag.getTagLocation().getX());
        insertStmt.setDouble(6, tag.getTagLocation().getY());
        insertStmt.setDouble(7, tag.getTagLocation().getZ());
        insertStmt.executeUpdate();
      }
      connection.commit();
    } catch (SQLException e) {
      throw new PatchPlaceBreakException("Failed to persist a patch place-and-break tag.", e);
    }
  }

  @Override
  public @NotNull Optional<Tag> findByLocation(@NotNull TagLocation tagLocation) {
    try (Connection connection = sqlDataSource.getConnection()) {
      String sqlQuery = String
          .format("SELECT * FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
              + " location_z = ?", SqlDataSource.SQL_TABLE_NAME);

      try (PreparedStatement queryStmt = connection.prepareStatement(sqlQuery)) {
        queryStmt.setString(1, tagLocation.getWorldName());
        queryStmt.setDouble(2, tagLocation.getX());
        queryStmt.setDouble(3, tagLocation.getY());
        queryStmt.setDouble(4, tagLocation.getZ());

        ResultSet rs = queryStmt.executeQuery();
        if (rs.getFetchSize() > 1) {
          logger.warning("Multiple tags detected for a same location, selecting the first one.");
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
      }
    } catch (SQLException e) {
      throw new PatchPlaceBreakException("Failed to fetch a patch place-and-break tag.", e);
    }
  }

  @Override
  public void delete(@NotNull TagLocation tagLocation) {
    try (Connection connection = sqlDataSource.getConnection()) {
      deleteTag(connection, tagLocation);
    } catch (SQLException e) {
      throw new PatchPlaceBreakException("Failed to delete a patch place-and-break tag.", e);
    }
  }

  private void deleteTag(@NotNull Connection connection, @NotNull TagLocation tagLocation)
      throws SQLException {
    String sqlDelete = String
        .format("DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
            + " location_z = ?", SqlDataSource.SQL_TABLE_NAME);

    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
      deleteStmt.setString(1, tagLocation.getWorldName());
      deleteStmt.setDouble(2, tagLocation.getX());
      deleteStmt.setDouble(3, tagLocation.getY());
      deleteStmt.setDouble(4, tagLocation.getZ());
      deleteStmt.executeUpdate();
    }
  }
}
