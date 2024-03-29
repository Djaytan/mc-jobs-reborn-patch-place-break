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
package fr.djaytan.mc.jrppb.core.storage.sql.access;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import fr.djaytan.mc.jrppb.api.entities.Tag;
import fr.djaytan.mc.jrppb.core.storage.api.OldNewBlockLocationPair;
import fr.djaytan.mc.jrppb.core.storage.api.OldNewBlockLocationPairSet;
import fr.djaytan.mc.jrppb.core.storage.api.TagRepository;
import fr.djaytan.mc.jrppb.core.storage.sql.DatabaseMediator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SqlTagRepository implements TagRepository {

  private final DatabaseMediator databaseMediator;
  private final TagSqlDao tagSqlDao;

  @Inject
  public SqlTagRepository(
      @NotNull DatabaseMediator databaseMediator, @NotNull TagSqlDao tagSqlDao) {
    this.databaseMediator = databaseMediator;
    this.tagSqlDao = tagSqlDao;
  }

  @Override
  public void put(@NotNull Tag tag) {
    databaseMediator.dispatchRequest(
        connection -> {
          try {
            connection.setAutoCommit(false);
            tagSqlDao.delete(connection, tag.blockLocation());
            tagSqlDao.insert(connection, tag);
            connection.commit();
          } catch (SQLException e) {
            throw new IllegalStateException(
                String.format("Failed to put the following tag: %s", tag), e);
          }
        });
  }

  @Override
  public void updateLocations(@NotNull OldNewBlockLocationPairSet oldNewLocationPairs) {
    databaseMediator.dispatchRequest(
        connection -> {
          try {
            connection.setAutoCommit(false);
            Set<Tag> newTags = prepareNewTags(connection, oldNewLocationPairs);
            cleanUpTags(connection, oldNewLocationPairs);
            putNewTags(connection, newTags);
            connection.commit();
          } catch (SQLException e) {
            throw new IllegalStateException(
                String.format(
                    "Failed to update the tags for the following old-new location pairs: %s",
                    oldNewLocationPairs),
                e);
          }
        });
  }

  private @NotNull Set<Tag> prepareNewTags(
      @NotNull Connection connection, @NotNull OldNewBlockLocationPairSet oldNewLocationPairs)
      throws SQLException {
    Set<Tag> newTags = new HashSet<>();

    for (OldNewBlockLocationPair oldNewLocationPair :
        oldNewLocationPairs.oldNewBlockLocationPairs()) {
      Optional<Tag> oldTag =
          tagSqlDao.findByLocation(connection, oldNewLocationPair.oldBlockLocation());

      if (oldTag.isEmpty()) {
        continue;
      }

      Tag newTag =
          new Tag(
              oldNewLocationPair.newBlockLocation(),
              oldTag.get().isEphemeral(),
              oldTag.get().createdAt());
      newTags.add(newTag);
    }

    return newTags;
  }

  private void cleanUpTags(
      @NotNull Connection connection,
      @NotNull OldNewBlockLocationPairSet oldNewBlockLocationPairSet)
      throws SQLException {
    Set<BlockLocation> tagsToRemove = oldNewBlockLocationPairSet.flattenBlockLocations();

    for (BlockLocation oldTagLocation : tagsToRemove) {
      tagSqlDao.delete(connection, oldTagLocation);
    }
  }

  private void putNewTags(@NotNull Connection connection, @NotNull Set<Tag> newTags)
      throws SQLException {
    for (Tag newTag : newTags) {
      tagSqlDao.insert(connection, newTag);
    }
  }

  @Override
  public @NotNull Optional<Tag> findByLocation(@NotNull BlockLocation blockLocation) {
    return databaseMediator.dispatchQuery(
        connection -> {
          try {
            return tagSqlDao.findByLocation(connection, blockLocation);
          } catch (SQLException e) {
            throw new IllegalStateException(
                String.format(
                    "Failed to fetch the tag with the following location: %s", blockLocation),
                e);
          }
        });
  }

  @Override
  public void delete(@NotNull BlockLocation blockLocation) {
    databaseMediator.dispatchRequest(
        connection -> {
          try {
            tagSqlDao.delete(connection, blockLocation);
          } catch (SQLException e) {
            throw new IllegalStateException(
                String.format(
                    "Failed to delete the tag with the following location: %s", blockLocation),
                e);
          }
        });
  }
}
