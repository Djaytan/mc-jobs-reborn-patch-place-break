/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Location;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.LocalDateTimeStringSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.UUIDStringSerializer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class TagSqlDao {

  private final BooleanIntegerSerializer booleanIntegerSerializer;
  private final DataSourceProperties dataSourceProperties;
  private final LocalDateTimeStringSerializer localDateTimeStringSerializer;
  private final UUIDStringSerializer uuidStringSerializer;

  @Inject
  public TagSqlDao(
      BooleanIntegerSerializer booleanIntegerSerializer,
      DataSourceProperties dataSourceProperties,
      LocalDateTimeStringSerializer localDateTimeStringSerializer,
      UUIDStringSerializer uuidStringSerializer) {
    this.booleanIntegerSerializer = booleanIntegerSerializer;
    this.dataSourceProperties = dataSourceProperties;
    this.localDateTimeStringSerializer = localDateTimeStringSerializer;
    this.uuidStringSerializer = uuidStringSerializer;
  }

  public void insert(@NonNull Connection connection, @NonNull Tag tag) throws SQLException {
    String sql =
        String.format(
            "INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?)", dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, uuidStringSerializer.serialize(tag.getUuid()));
      preparedStatement.setString(
          2, localDateTimeStringSerializer.serialize(tag.getInitLocalDateTime()));
      preparedStatement.setInt(3, booleanIntegerSerializer.serialize(tag.isEphemeral()));
      preparedStatement.setString(4, tag.getLocation().getWorldName());
      preparedStatement.setDouble(5, tag.getLocation().getX());
      preparedStatement.setDouble(6, tag.getLocation().getY());
      preparedStatement.setDouble(7, tag.getLocation().getZ());
      preparedStatement.executeUpdate();
    }
  }

  public @NonNull Optional<Tag> findByLocation(
      @NonNull Connection connection, @NonNull Location location) throws SQLException {
    String sql =
        String.format(
            "SELECT * FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, location.getWorldName());
      preparedStatement.setDouble(2, location.getX());
      preparedStatement.setDouble(3, location.getY());
      preparedStatement.setDouble(4, location.getZ());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return extractTag(resultSet);
      }
    }
  }

  private @NonNull Optional<Tag> extractTag(@NonNull ResultSet resultSet) throws SQLException {
    if (resultSet.getFetchSize() > 1) {
      log.atWarn()
          .log(
              "Multiple tags detected for a same location, selecting the first one."
                  + " Anyway, please report this issue to the developer for investigation.");
    }

    if (!resultSet.next()) {
      return Optional.empty();
    }

    UUID tagUuid = uuidStringSerializer.deserialize(resultSet.getString("tag_uuid"));
    LocalDateTime initLocalDateTime =
        localDateTimeStringSerializer.deserialize(resultSet.getString("init_timestamp"));
    boolean isEphemeral = booleanIntegerSerializer.deserialize(resultSet.getInt("is_ephemeral"));
    String worldName = resultSet.getString("world_name");
    double x = resultSet.getDouble("location_x");
    double y = resultSet.getDouble("location_y");
    double z = resultSet.getDouble("location_z");

    Location location = Location.of(worldName, x, y, z);
    Tag tag = Tag.of(tagUuid, initLocalDateTime, isEphemeral, location);
    return Optional.of(tag);
  }

  public void delete(@NonNull Connection connection, @NonNull Location location)
      throws SQLException {
    String sqlDelete =
        String.format(
            "DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
      deleteStmt.setString(1, location.getWorldName());
      deleteStmt.setDouble(2, location.getX());
      deleteStmt.setDouble(3, location.getY());
      deleteStmt.setDouble(4, location.getZ());
      deleteStmt.executeUpdate();
    }
  }
}
