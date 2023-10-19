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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;

class SqlTagRepositoryIntegrationTest {

  private BlockLocation randomBlockLocation;
  private final MysqlTagRepositoryFactory mysqlTagRepositoryFactory =
      new MysqlTagRepositoryFactory();
  private SqlTagRepository sqlTagRepository;

  @BeforeEach
  void beforeEach() {
    sqlTagRepository = mysqlTagRepositoryFactory.create();
    randomBlockLocation = createRandomBlockLocation();
  }

  @AfterEach
  void afterEach() {
    mysqlTagRepositoryFactory.close();
  }

  @Test
  @DisplayName("When retrieving tag when it doesn't exist")
  void whenRetrievingTagWhenItDoesntExist_shouldGetEmptyValue() {
    // Given

    // When
    Optional<Tag> tag = sqlTagRepository.findByLocation(randomBlockLocation);

    // Then
    assertThat(tag).isEmpty();
  }

  @Test
  @DisplayName("When putting tag when no one already exist")
  void whenPuttingTagWhenNoOneAlreadyExist_shouldThenBeRetrievable() {
    // Given
    Preconditions.condition(
        sqlTagRepository.findByLocation(randomBlockLocation).isEmpty(),
        String.format("No tag must exist at the following location: %s", randomBlockLocation));

    Tag tag = new Tag(randomBlockLocation, true, LocalDateTime.now());

    // When
    sqlTagRepository.put(tag);

    // Then
    Optional<Tag> retrievedTag = sqlTagRepository.findByLocation(randomBlockLocation);

    assertThat(retrievedTag).isPresent().contains(tag);
  }

  @Test
  @DisplayName("When putting tag when one already exist")
  void whenPuttingTagWhenOneAlreadyExist_shouldOverrideIt() {
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

  @Test
  @DisplayName("When updating existing tag")
  void whenUpdatingExistingTag_shouldExistInNewLocationButNotInOldOne() {
    // Given
    BlockLocation oldLocation = randomBlockLocation;
    Tag oldTag = new Tag(oldLocation, true, LocalDateTime.now());

    BlockLocation newLocation =
        new BlockLocation(
            oldLocation.worldName(), oldLocation.x() + 1, oldLocation.y(), oldLocation.z());

    Preconditions.condition(
        sqlTagRepository.findByLocation(newLocation).isEmpty(),
        String.format("No tag must exist at the following location: %s", newLocation));

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
  @DisplayName("When updating non-existing tag")
  void whenUpdatingNonExistingTag_shouldExistInNewLocationButNotInOldOne() {
    // Given
    BlockLocation oldLocation = randomBlockLocation;
    BlockLocation newLocation =
        new BlockLocation(
            oldLocation.worldName(), oldLocation.x() + 1, oldLocation.y(), oldLocation.z());

    Preconditions.condition(
        sqlTagRepository.findByLocation(oldLocation).isEmpty(),
        String.format("No tag must exist at the following location: %s", oldLocation));
    Preconditions.condition(
        sqlTagRepository.findByLocation(newLocation).isEmpty(),
        String.format("No tag must exist at the following location: %s", newLocation));

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

  @Test
  @DisplayName("When removing tag")
  void whenRemovingTag_shouldThenNotExistAnymore() {
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
