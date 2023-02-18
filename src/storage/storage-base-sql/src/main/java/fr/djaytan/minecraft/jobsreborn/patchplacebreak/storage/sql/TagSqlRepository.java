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

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.TagRepositoryException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TagSqlRepository implements TagRepository {

  private final ConnectionPool connectionPool;
  private final TagSqlDao tagSqlDao;

  @Override
  public void put(@NonNull Tag tag) {
    try (Connection connection = connectionPool.getConnection()) {
      try {
        connection.setAutoCommit(false);
        tagSqlDao.delete(connection, tag.getTagLocation());
        tagSqlDao.insert(connection, tag);
        connection.commit();
      } catch (SQLException e) {
        throw TagRepositoryException.put(tag, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }

  @Override
  public @NonNull Optional<Tag> findByLocation(@NonNull TagLocation tagLocation) {
    try (Connection connection = connectionPool.getConnection()) {
      try {
        return tagSqlDao.findByLocation(connection, tagLocation);
      } catch (SQLException e) {
        throw TagRepositoryException.fetch(tagLocation, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }

  @Override
  public void delete(@NonNull TagLocation tagLocation) {
    try (Connection connection = connectionPool.getConnection()) {
      try {
        tagSqlDao.delete(connection, tagLocation);
      } catch (SQLException e) {
        throw TagRepositoryException.delete(tagLocation, e);
      }
    } catch (SQLException e) {
      throw SqlStorageException.databaseConnectionReleasing(e);
    }
  }
}
