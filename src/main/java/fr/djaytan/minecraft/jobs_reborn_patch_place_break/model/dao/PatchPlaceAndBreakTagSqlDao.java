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

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.dao;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.PatchPlaceAndBreakRuntimeException;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.PatchPlaceAndBreakTag;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.serializer.LocalDateTimeStringSerializer;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.serializer.UUIDStringSerializer;
import fr.djaytan.minecraft.jobs_reborn_patch_place_break.plugin.datasource.SqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class PatchPlaceAndBreakTagSqlDao implements PatchPlaceAndBreakTagDao {

  private final BooleanIntegerSerializer booleanIntegerSerializer;
  private final SqlDataSource sqlDataSource;
  private final LocalDateTimeStringSerializer localDateTimeStringSerializer;
  private final Logger logger;
  private final UUIDStringSerializer uuidStringSerializer;

  @Inject
  public PatchPlaceAndBreakTagSqlDao(
      @NotNull BooleanIntegerSerializer booleanIntegerSerializer,
      @NotNull SqlDataSource sqlDataSource,
      @NotNull LocalDateTimeStringSerializer localDateTimeStringSerializer,
      @NotNull Logger logger,
      @NotNull UUIDStringSerializer uuidStringSerializer) {
    this.booleanIntegerSerializer = booleanIntegerSerializer;
    this.sqlDataSource = sqlDataSource;
    this.localDateTimeStringSerializer = localDateTimeStringSerializer;
    this.logger = logger;
    this.uuidStringSerializer = uuidStringSerializer;
  }

  @Override
  public void persist(@NotNull PatchPlaceAndBreakTag patchPlaceAndBreakTag) {
    Connection connection = sqlDataSource.getConnection();

    String sqlInsert =
        String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?)", SqlDataSource.TABLE_NAME);

    try (PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)) {
      insertStmt.setString(1, uuidStringSerializer.serialize(patchPlaceAndBreakTag.getUuid()));
      insertStmt.setString(
          2, localDateTimeStringSerializer.serialize(patchPlaceAndBreakTag.getInitLocalDateTime()));
      insertStmt.setInt(3, booleanIntegerSerializer.serialize(patchPlaceAndBreakTag.isEphemeral()));
      insertStmt.setString(4, patchPlaceAndBreakTag.getTagLocation().getWorldName());
      insertStmt.setDouble(5, patchPlaceAndBreakTag.getTagLocation().getX());
      insertStmt.setDouble(6, patchPlaceAndBreakTag.getTagLocation().getY());
      insertStmt.setDouble(7, patchPlaceAndBreakTag.getTagLocation().getZ());
      insertStmt.executeUpdate();
    } catch (SQLException e) {
      throw new PatchPlaceAndBreakRuntimeException(
          "Failed to persist a patch place-and-break tag.", e);
    }
  }

  @Override
  public @NotNull Optional<PatchPlaceAndBreakTag> findByLocation(@NotNull TagLocation tagLocation) {
    Connection connection = sqlDataSource.getConnection();

    String sqlQuery =
        String.format(
            "SELECT * FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            SqlDataSource.TABLE_NAME);

    try (PreparedStatement queryStmt = connection.prepareStatement(sqlQuery)) {
      queryStmt.setString(1, tagLocation.getWorldName());
      queryStmt.setDouble(2, tagLocation.getX());
      queryStmt.setDouble(3, tagLocation.getY());
      queryStmt.setDouble(4, tagLocation.getZ());

      ResultSet rs = queryStmt.executeQuery();
      if (rs.getFetchSize() > 1) {
        logger.warn("Multiple tags detected for a same location, selecting the first one.");
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

        TagLocation location = new TagLocation(worldName, x, y, z);
        PatchPlaceAndBreakTag tag =
            new PatchPlaceAndBreakTag(tagUuid, initLocalDateTime, isEphemeral, location);
        return Optional.of(tag);
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new PatchPlaceAndBreakRuntimeException(
          "Failed to fetch a patch place-and-break tag.", e);
    }
  }

  @Override
  public void delete(@NotNull TagLocation tagLocation) {
    Connection connection = sqlDataSource.getConnection();

    String sqlDelete =
        String.format(
            "DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            SqlDataSource.TABLE_NAME);

    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
      deleteStmt.setString(1, tagLocation.getWorldName());
      deleteStmt.setDouble(2, tagLocation.getX());
      deleteStmt.setDouble(3, tagLocation.getY());
      deleteStmt.setDouble(4, tagLocation.getZ());
      deleteStmt.executeUpdate();
    } catch (SQLException e) {
      throw new PatchPlaceAndBreakRuntimeException(
          "Failed to delete a patch place-and-break tag.", e);
    }
  }
}
