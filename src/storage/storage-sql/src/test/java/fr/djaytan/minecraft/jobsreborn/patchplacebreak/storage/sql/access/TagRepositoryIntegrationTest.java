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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPair;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPairSet;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.TagRepository;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.ConnectionPool;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.DataSourcePropertiesMock;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.JdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlDataSourceManager;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.mysql.MysqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.mysql.MysqlJdbcUrl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataMigrationExecutor;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init.DataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.LocalDateTimeStringSerializer;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.NonNull;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.flywaydb.core.api.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class TagRepositoryIntegrationTest {

  private static TagRepository tagRepository;

  @BeforeAll
  static void beforeAll() {
    DataSourceProperties dataSourcePropertiesMocked = DataSourcePropertiesMock.get();
    int dbmsPort = Integer.parseInt(System.getProperty("mysql.port"));
    given(dataSourcePropertiesMocked.getDbmsServer().getHost().getPort()).willReturn(dbmsPort);
    JdbcUrl jdbcUrl = new MysqlJdbcUrl(dataSourcePropertiesMocked);
    ConnectionPool connectionPool = new ConnectionPool(dataSourcePropertiesMocked, jdbcUrl);

    ClassLoader classLoader = DataMigrationExecutor.class.getClassLoader();
    Location location = new Location("/db/migration/mysql");
    DataMigrationExecutor dataMigrationExecutor =
        new DataMigrationExecutor(classLoader, dataSourcePropertiesMocked, location);

    DataSourceInitializer dataSourceInitializer = new MysqlDataSourceInitializer();

    DataSourceManager dataSourceManager =
        new SqlDataSourceManager(connectionPool, dataMigrationExecutor, dataSourceInitializer);
    dataSourceManager.connect();

    BooleanIntegerSerializer booleanIntegerSerializer = new BooleanIntegerSerializer();
    LocalDateTimeStringSerializer localDateTimeStringSerializer =
        new LocalDateTimeStringSerializer();
    TagSqlDao tagSqlDao =
        new TagSqlDao(
            booleanIntegerSerializer, dataSourcePropertiesMocked, localDateTimeStringSerializer);

    tagRepository = new SqlTagRepository(connectionPool, tagSqlDao);
  }

  @Test
  @Order(1)
  @DisplayName("When putting tag")
  void whenPuttingTag_shouldNotThrow() {
    // Given
    Tag tag = Tag.of(BlockLocation.of("world", 12, 45, -234), false, LocalDateTime.now());

    // When
    ThrowingCallable throwingCallable = () -> tagRepository.put(tag);

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(2)
  @DisplayName("When checking exploit with existing tag")
  void whenCheckingExploit_withExistingTag_shouldExists() {
    assertTagExists(BlockLocation.of("world", 12, 45, -234));
  }

  @Test
  @Order(3)
  @DisplayName("When moving tag")
  void whenMovingTag_shouldNotThrow() {
    // Given
    OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
        new OldNewBlockLocationPairSet(
            Collections.singletonList(
                new OldNewBlockLocationPair(
                    BlockLocation.of("world", 12, 45, -234),
                    BlockLocation.of("world", 13, 45, -234))));

    // When
    ThrowingCallable throwingCallable =
        () -> tagRepository.updateLocations(oldNewBlockLocationPairSet);

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(4)
  @DisplayName("When checking exploit with moved tag")
  void whenCheckingExploit_withMovedTag_shouldExistsInNewLocationButNotInOldOne() {
    assertTagDoesNotExists(BlockLocation.of("world", 12, 45, -234));
    assertTagExists(BlockLocation.of("world", 13, 45, -234));
  }

  @Test
  @Order(5)
  @DisplayName("When removing tag")
  void whenRemovingTag_shouldRemoveTagFromStorage() {
    // Given
    BlockLocation blockLocation = BlockLocation.of("world", 13, 45, -234);

    // When
    ThrowingCallable throwingCallable = () -> tagRepository.delete(blockLocation);

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(6)
  @DisplayName("When checking exploit without existing tag")
  void whenCheckingExploit_withoutExistingTag_shouldNotExists() {
    assertTagDoesNotExists(BlockLocation.of("world", 13, 45, -234));
  }

  private void assertTagExists(@NonNull BlockLocation blockLocation) {
    assertTagExistence(blockLocation, true);
  }

  private void assertTagDoesNotExists(@NonNull BlockLocation blockLocation) {
    assertTagExistence(blockLocation, false);
  }

  private void assertTagExistence(
      @NonNull BlockLocation blockLocation, boolean expectedTagExistence) {
    // Given

    // When
    boolean actualTagExistence = tagRepository.findByLocation(blockLocation).isPresent();

    // Then
    assertThat(actualTagExistence).isEqualTo(expectedTagExistence);
  }
}
