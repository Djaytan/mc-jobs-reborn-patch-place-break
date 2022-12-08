/*
 * JobsReborn extension to patch place-break (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.PatchPlaceAndBreakTagDao;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.SqlDataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite.serializer.LocalDateTimeStringSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.sqlite.serializer.UUIDStringSerializer;

@Singleton
public class PatchPlaceAndBreakTagSqliteDao implements PatchPlaceAndBreakTagDao {

  private final BooleanIntegerSerializer booleanIntegerSerializer;
  private final SqlDataSource sqlDataSource;
  private final LocalDateTimeStringSerializer localDateTimeStringSerializer;
  private final Logger logger;
  private final UUIDStringSerializer uuidStringSerializer;

  @Inject
  public PatchPlaceAndBreakTagSqliteDao(@NotNull BooleanIntegerSerializer booleanIntegerSerializer,
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

      try (PreparedStatement deleteStmt = getDeleteStatement(connection, tag.getTagLocation())) {
        deleteStmt.executeUpdate();
      }

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
      try (PreparedStatement deleteStmt = getDeleteStatement(connection, tagLocation)) {
        deleteStmt.executeUpdate();
      }
    } catch (SQLException e) {
      throw new PatchPlaceBreakException("Failed to delete a patch place-and-break tag.", e);
    }
  }

  private @NotNull PreparedStatement getDeleteStatement(@NotNull Connection connection,
      @NotNull TagLocation tagLocation) throws SQLException {
    String sqlDelete = String
        .format("DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
            + " location_z = ?", SqlDataSource.SQL_TABLE_NAME);

    PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete);
    deleteStmt.setString(1, tagLocation.getWorldName());
    deleteStmt.setDouble(2, tagLocation.getX());
    deleteStmt.setDouble(3, tagLocation.getY());
    deleteStmt.setDouble(4, tagLocation.getZ());
    return deleteStmt;
  }
}
