/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.access;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.LocalDateTimeStringSerializer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TagSqlDao {

  private static final Logger LOG = LoggerFactory.getLogger(TagSqlDao.class);

  private final BooleanIntegerSerializer booleanIntegerSerializer;
  private final DataSourceProperties dataSourceProperties;
  private final LocalDateTimeStringSerializer localDateTimeStringSerializer;

  @Inject
  public TagSqlDao(
      @NotNull BooleanIntegerSerializer booleanIntegerSerializer,
      @NotNull DataSourceProperties dataSourceProperties,
      @NotNull LocalDateTimeStringSerializer localDateTimeStringSerializer) {
    this.booleanIntegerSerializer = booleanIntegerSerializer;
    this.dataSourceProperties = dataSourceProperties;
    this.localDateTimeStringSerializer = localDateTimeStringSerializer;
  }

  public void insert(@NotNull Connection connection, @NotNull Tag tag) throws SQLException {
    String sql =
        String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?)", dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, tag.blockLocation().worldName());
      preparedStatement.setInt(2, tag.blockLocation().x());
      preparedStatement.setInt(3, tag.blockLocation().y());
      preparedStatement.setInt(4, tag.blockLocation().z());
      preparedStatement.setInt(5, booleanIntegerSerializer.serialize(tag.isEphemeral()));
      preparedStatement.setString(
          6, localDateTimeStringSerializer.serialize(tag.initLocalDateTime()));
      preparedStatement.executeUpdate();
    }
  }

  public @NotNull Optional<Tag> findByLocation(
      @NotNull Connection connection, @NotNull BlockLocation blockLocation) throws SQLException {
    String sql =
        String.format(
            "SELECT * FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, blockLocation.worldName());
      preparedStatement.setInt(2, blockLocation.x());
      preparedStatement.setInt(3, blockLocation.y());
      preparedStatement.setInt(4, blockLocation.z());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return extractTag(resultSet);
      }
    }
  }

  private @NotNull Optional<Tag> extractTag(@NotNull ResultSet resultSet) throws SQLException {
    if (resultSet.getFetchSize() > 1) {
      LOG.atWarn()
          .log(
              "Multiple tags detected for a same location, selecting the first one."
                  + " Anyway, please report this issue to the developer for investigation.");
    }

    if (!resultSet.next()) {
      return Optional.empty();
    }

    String worldName = resultSet.getString("world_name");
    int x = resultSet.getInt("location_x");
    int y = resultSet.getInt("location_y");
    int z = resultSet.getInt("location_z");
    BlockLocation blockLocation = new BlockLocation(worldName, x, y, z);

    boolean isEphemeral = booleanIntegerSerializer.deserialize(resultSet.getInt("is_ephemeral"));
    LocalDateTime initLocalDateTime =
        localDateTimeStringSerializer.deserialize(resultSet.getString("init_timestamp"));

    Tag tag = new Tag(blockLocation, isEphemeral, initLocalDateTime);
    return Optional.of(tag);
  }

  public void delete(@NotNull Connection connection, @NotNull BlockLocation blockLocation)
      throws SQLException {
    String sqlDelete =
        String.format(
            "DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
      deleteStmt.setString(1, blockLocation.worldName());
      deleteStmt.setInt(2, blockLocation.x());
      deleteStmt.setInt(3, blockLocation.y());
      deleteStmt.setInt(4, blockLocation.z());
      deleteStmt.executeUpdate();
    }
  }
}
