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

import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_DATABASE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import fr.djaytan.mc.jrppb.api.entities.Tag;
import fr.djaytan.mc.jrppb.core.storage.api.OldNewBlockLocationPair;
import fr.djaytan.mc.jrppb.core.storage.api.OldNewBlockLocationPairSet;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

class SqlTagRepositoryIT {

  private static final int DATABASE_ORIGINAL_PORT = 3306;

  @SuppressWarnings("resource") // Reusable containers feature enabled: do not clean-up containers!
  private static final MySQLContainer<?> MYSQL_CONTAINER =
      new MySQLContainer<>("mysql:8.1.0-oracle")
          .withDatabaseName(NOMINAL_DBMS_SERVER_DATABASE_NAME)
          .withReuse(true);

  @AutoClose
  private final MysqlTagRepositoryFactory mysqlTagRepositoryFactory =
      new MysqlTagRepositoryFactory();

  private BlockLocation randomBlockLocation;
  private SqlTagRepository sqlTagRepository;

  @BeforeAll
  static void beforeAll() {
    MYSQL_CONTAINER.start();
  }

  @BeforeEach
  void setUp() {
    int dbmsPort = MYSQL_CONTAINER.getMappedPort(DATABASE_ORIGINAL_PORT);
    String username = MYSQL_CONTAINER.getUsername();
    String password = MYSQL_CONTAINER.getPassword();

    sqlTagRepository = mysqlTagRepositoryFactory.create(dbmsPort, username, password);
    randomBlockLocation = createRandomBlockLocation();
  }

  @Test
  void whenRetrievingTagWhenItDoesntExist_shouldGetEmptyValue() {
    assertThat(sqlTagRepository.findByLocation(randomBlockLocation)).isEmpty();
  }

  @Nested
  class WhenPuttingTag {

    @Test
    void whenNoOneAlreadyExist_shouldThenBeRetrievable() {
      // Given
      Tag tag = new Tag(randomBlockLocation, true, LocalDateTime.now());

      // When
      sqlTagRepository.put(tag);

      // Then
      Optional<Tag> retrievedTag = sqlTagRepository.findByLocation(randomBlockLocation);

      assertThat(retrievedTag).isPresent().contains(tag);
    }

    @Test
    void whenOneAlreadyExist_shouldOverrideIt() {
      // Given
      LocalDateTime firstDateTime = LocalDateTime.now();
      Tag alreadyExistingTag = new Tag(randomBlockLocation, true, firstDateTime);

      sqlTagRepository.put(alreadyExistingTag);

      LocalDateTime secondDateTime = firstDateTime.plusSeconds(1);
      Tag overrideTag = new Tag(randomBlockLocation, true, secondDateTime);

      // When
      sqlTagRepository.put(overrideTag);

      // Then
      Optional<Tag> retrievedTag = sqlTagRepository.findByLocation(randomBlockLocation);

      assertThat(retrievedTag).isPresent().contains(overrideTag);
    }
  }

  @Nested
  class WhenUpdatingTagLocation {

    @Test
    void whenTagExists_shouldExistInNewLocationButNotInOldOne() {
      // Given
      BlockLocation oldLocation = randomBlockLocation;
      Tag oldTag = new Tag(oldLocation, true, LocalDateTime.now());

      BlockLocation newLocation =
          new BlockLocation(
              oldLocation.worldName(), oldLocation.x() + 1, oldLocation.y(), oldLocation.z());

      sqlTagRepository.put(oldTag);

      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(
              Collections.singletonList(new OldNewBlockLocationPair(oldLocation, newLocation)));

      // When
      sqlTagRepository.updateLocations(oldNewBlockLocationPairSet);

      // Then
      Optional<Tag> tagOldLocation = sqlTagRepository.findByLocation(oldLocation);
      Optional<Tag> tagNewLocation = sqlTagRepository.findByLocation(newLocation);

      assertAll(
          () -> assertThat(tagOldLocation).isEmpty(), () -> assertThat(tagNewLocation).isPresent());
    }

    @Test
    void whenTagDoesntExist_shouldNotCreateTagInAnyLocation() {
      // Given
      BlockLocation oldLocation = randomBlockLocation;
      BlockLocation newLocation =
          new BlockLocation(
              oldLocation.worldName(), oldLocation.x() + 1, oldLocation.y(), oldLocation.z());

      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(
              Collections.singletonList(new OldNewBlockLocationPair(oldLocation, newLocation)));

      // When
      sqlTagRepository.updateLocations(oldNewBlockLocationPairSet);

      // Then
      Optional<Tag> tagOldLocation = sqlTagRepository.findByLocation(oldLocation);
      Optional<Tag> tagNewLocation = sqlTagRepository.findByLocation(newLocation);

      assertAll(
          () -> assertThat(tagOldLocation).isEmpty(), () -> assertThat(tagNewLocation).isEmpty());
    }
  }

  @Test
  void whenRemovingTag_shouldNotExistAnymore() {
    // Given
    Tag alreadyExistingTag = new Tag(randomBlockLocation, true, LocalDateTime.now());
    sqlTagRepository.put(alreadyExistingTag);

    // When
    sqlTagRepository.delete(randomBlockLocation);

    // Then
    Optional<Tag> retrievedTag = sqlTagRepository.findByLocation(randomBlockLocation);

    assertThat(retrievedTag).isEmpty();
  }

  /* Helpers */

  private @NotNull BlockLocation createRandomBlockLocation() {
    Random random = new Random();
    int randX = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    int randY = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    int randZ = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    return new BlockLocation("world", randX, randY, randZ);
  }
}
