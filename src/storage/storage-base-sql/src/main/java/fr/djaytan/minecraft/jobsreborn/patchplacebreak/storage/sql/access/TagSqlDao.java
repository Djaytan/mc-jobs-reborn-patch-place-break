package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.access;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
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
      preparedStatement.setString(4, tag.getBlockLocation().getWorldName());
      preparedStatement.setInt(5, tag.getBlockLocation().getX());
      preparedStatement.setInt(6, tag.getBlockLocation().getY());
      preparedStatement.setInt(7, tag.getBlockLocation().getZ());
      preparedStatement.executeUpdate();
    }
  }

  public @NonNull Optional<Tag> findByLocation(
      @NonNull Connection connection, @NonNull BlockLocation blockLocation) throws SQLException {
    String sql =
        String.format(
            "SELECT * FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, blockLocation.getWorldName());
      preparedStatement.setInt(2, blockLocation.getX());
      preparedStatement.setInt(3, blockLocation.getY());
      preparedStatement.setInt(4, blockLocation.getZ());

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

    int x = resultSet.getInt("location_x");
    int y = resultSet.getInt("location_y");
    int z = resultSet.getInt("location_z");

    BlockLocation blockLocation = BlockLocation.of(worldName, x, y, z);
    Tag tag = Tag.of(tagUuid, initLocalDateTime, isEphemeral, blockLocation);
    return Optional.of(tag);
  }

  public void delete(@NonNull Connection connection, @NonNull BlockLocation blockLocation)
      throws SQLException {
    String sqlDelete =
        String.format(
            "DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
      deleteStmt.setString(1, blockLocation.getWorldName());
      deleteStmt.setInt(2, blockLocation.getX());
      deleteStmt.setInt(3, blockLocation.getY());
      deleteStmt.setInt(4, blockLocation.getZ());
      deleteStmt.executeUpdate();
    }
  }
}
