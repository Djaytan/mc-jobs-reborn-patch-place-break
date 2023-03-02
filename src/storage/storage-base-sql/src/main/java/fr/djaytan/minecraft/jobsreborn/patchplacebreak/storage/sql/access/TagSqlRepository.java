package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.access;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.TagRepositoryException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.ConnectionPool;
import java.sql.SQLException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public class TagSqlRepository implements TagRepository {

  private final ConnectionPool connectionPool;
  private final TagSqlDao tagSqlDao;

  @Inject
  public TagSqlRepository(ConnectionPool connectionPool, TagSqlDao tagSqlDao) {
    this.connectionPool = connectionPool;
    this.tagSqlDao = tagSqlDao;
  }

  @Override
  public void put(@NonNull Tag tag) {
    connectionPool.useConnection(
        connection -> {
          try {
            connection.setAutoCommit(false);
            tagSqlDao.delete(connection, tag.getBlockLocation());
            tagSqlDao.insert(connection, tag);
            connection.commit();
          } catch (SQLException e) {
            throw TagRepositoryException.put(tag, e);
          }
        });
  }

  @Override
  public @NonNull Optional<Tag> findByLocation(@NonNull BlockLocation blockLocation) {
    return connectionPool.useConnection(
        connection -> {
          try {
            return tagSqlDao.findByLocation(connection, blockLocation);
          } catch (SQLException e) {
            throw TagRepositoryException.fetch(blockLocation, e);
          }
        });
  }

  @Override
  public void delete(@NonNull BlockLocation blockLocation) {
    connectionPool.useConnection(
        connection -> {
          try {
            tagSqlDao.delete(connection, blockLocation);
          } catch (SQLException e) {
            throw TagRepositoryException.delete(blockLocation, e);
          }
        });
  }
}
