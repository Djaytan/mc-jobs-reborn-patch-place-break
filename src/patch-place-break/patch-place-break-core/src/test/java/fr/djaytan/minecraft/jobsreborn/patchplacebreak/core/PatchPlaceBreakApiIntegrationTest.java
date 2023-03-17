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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class PatchPlaceBreakApiIntegrationTest {

  private static Path dataFolder;
  private static PatchPlaceBreakCore patchPlaceBreakCore;
  private static PatchPlaceBreakApi patchPlaceBreakApi;

  @BeforeAll
  @SneakyThrows
  static void beforeAll() {
    dataFolder = Files.createTempDirectory("ppb-api-it");
    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();
    patchPlaceBreakCore = new PatchPlaceBreakCore();
    patchPlaceBreakApi = patchPlaceBreakCore.enable(classLoader, dataFolder);
  }

  @AfterAll
  @SneakyThrows
  static void afterAll() {
    patchPlaceBreakCore.disable();
    FileUtils.deleteDirectory(dataFolder.toFile());
  }

  @Test
  @Order(1)
  @DisplayName("When putting tag")
  void whenPuttingTag_shouldNotThrow() {
    // Given
    BlockLocation blockLocation = BlockLocation.of("world", 12, 45, -234);
    boolean isEphemeral = false;

    // When
    ThrowingCallable throwingCallable =
        () -> patchPlaceBreakApi.putTag(blockLocation, isEphemeral).join();

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
    Vector direction = Vector.of(1, 0, 0);
    BlockLocation blockLocation = BlockLocation.of("world", 12, 45, -234);
    Set<BlockLocation> blockLocationSet = new HashSet<>(Collections.singletonList(blockLocation));

    // When
    ThrowingCallable throwingCallable =
        () -> patchPlaceBreakApi.moveTags(blockLocationSet, direction).join();

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(4)
  @DisplayName("When checking exploit with moved tag")
  void whenCheckingExploit_withMovedTag_shouldExistsInNewLocationButNotInOldOne() {
    assertAll(
        () -> assertTagDoesNotExists(BlockLocation.of("world", 12, 45, -234)),
        () -> assertTagExists(BlockLocation.of("world", 13, 45, -234)));
  }

  @Test
  @Order(5)
  @DisplayName("When removing tag")
  void whenRemovingTag_shouldRemoveTagFromStorage() {
    // Given
    BlockLocation blockLocation = BlockLocation.of("world", 13, 45, -234);

    // When
    ThrowingCallable throwingCallable = () -> patchPlaceBreakApi.removeTag(blockLocation).join();

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
    BlockActionType blockActionType = BlockActionType.BREAK;

    // When
    boolean actualTagExistence =
        patchPlaceBreakApi.isPlaceAndBreakExploit(blockActionType, blockLocation);

    // Then
    assertThat(actualTagExistence).isEqualTo(expectedTagExistence);
  }
}
