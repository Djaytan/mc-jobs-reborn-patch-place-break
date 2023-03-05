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
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Block;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
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
    Block block = Block.of(BlockLocation.of("world", 12, 45, -234), "STONE");
    boolean isEphemeral = false;

    // When
    ThrowingCallable throwingCallable = () -> patchPlaceBreakApi.putTag(block, isEphemeral).join();

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(2)
  @DisplayName("When checking exploit with existing tag")
  void whenCheckingExploit_withExistingTag_shouldDetectExploit() {
    // Given
    BlockActionType blockActionType = BlockActionType.BREAK;
    Block block = Block.of(BlockLocation.of("world", 12, 45, -234), "STONE");

    // When
    boolean isExploit = patchPlaceBreakApi.isPlaceAndBreakExploit(blockActionType, block);

    // Then
    assertThat(isExploit).isTrue();
  }

  @Test
  @Order(3)
  @DisplayName("When moving tag")
  void whenMovingTag_shouldNotThrow() {
    // Given
    Vector direction = Vector.of(1, 0, 0);
    Block block = Block.of(BlockLocation.of("world", 12, 45, -234), "STONE");
    Set<Block> blockSet = Collections.singleton(block);

    // When
    ThrowingCallable throwingCallable =
        () -> patchPlaceBreakApi.moveTags(blockSet, direction).join();

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(4)
  @DisplayName("When checking exploit with moved tag")
  void whenCheckingExploit_withMovedTag_shouldDetectExploitInNewLocationButNotInOldOne() {
    // Given
    BlockActionType blockActionType = BlockActionType.BREAK;
    Block oldBlock = Block.of(BlockLocation.of("world", 12, 45, -234), "STONE");
    Block newBlock = Block.of(BlockLocation.of("world", 13, 45, -234), "STONE");

    // When
    boolean isOnOldLocationAnExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(blockActionType, oldBlock);
    boolean isOnNewLocationAnExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(blockActionType, newBlock);

    // Then
    assertAll(
        () -> assertThat(isOnOldLocationAnExploit).isFalse(),
        () -> assertThat(isOnNewLocationAnExploit).isTrue());
  }

  @Test
  @Order(5)
  @DisplayName("When removing tag")
  void whenRemovingTag_shouldNotThrow() {
    // Given
    Block block = Block.of(BlockLocation.of("world", 13, 45, -234), "STONE");

    // When
    ThrowingCallable throwingCallable = () -> patchPlaceBreakApi.removeTag(block).join();

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(6)
  @DisplayName("When checking exploit without existing tag")
  void whenCheckingExploit_withoutExistingTag_shouldNotDetectExploit() {
    // Given
    BlockActionType blockActionType = BlockActionType.BREAK;
    Block block = Block.of(BlockLocation.of("world", 13, 45, -234), "STONE");

    // When
    boolean isExploit = patchPlaceBreakApi.isPlaceAndBreakExploit(blockActionType, block);

    // Then
    assertThat(isExploit).isFalse();
  }
}
