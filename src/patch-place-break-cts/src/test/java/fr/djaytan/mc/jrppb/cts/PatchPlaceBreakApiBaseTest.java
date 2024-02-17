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
package fr.djaytan.mc.jrppb.cts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import fr.djaytan.mc.jrppb.api.PatchPlaceBreakApi;
import fr.djaytan.mc.jrppb.api.entities.Block;
import fr.djaytan.mc.jrppb.api.entities.BlockActionType;
import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import fr.djaytan.mc.jrppb.api.entities.Vector;
import fr.djaytan.mc.jrppb.core.PatchPlaceBreakCore;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.threeten.extra.MutableClock;

abstract class PatchPlaceBreakApiBaseTest {

  private final BlockLocation randomBlockLocation = generateRandomBlockLocation();
  private final MutableClock mutableClock = MutableClock.epochUTC();
  private PatchPlaceBreakApi patchPlaceBreakApi;
  private final PatchPlaceBreakCore patchPlaceBreakCore = new PatchPlaceBreakCore();
  @TempDir protected Path dataFolder;

  @BeforeEach
  void beforeEach() throws IOException {
    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();
    patchPlaceBreakApi = patchPlaceBreakCore.enable(classLoader, mutableClock, dataFolder);
  }

  @AfterEach
  void afterEach() {
    patchPlaceBreakCore.disable();
  }

  @Test
  void whenTagDoesntExist_shouldNotDetectExploit() {
    assertThat(
            patchPlaceBreakApi.isPlaceAndBreakExploit(
                BlockActionType.BREAK, new Block(randomBlockLocation, "STONE")))
        .isFalse();
  }

  @ParameterizedTest
  @MethodSource
  void whenPuttingTag(
      boolean isEphemeral, @NotNull TemporalAmount timeElapsedAfterPut, boolean isExploitExpected) {
    // Given
    Block block = new Block(randomBlockLocation, "STONE");

    // When
    patchPlaceBreakApi.putTag(block, isEphemeral).join();
    mutableClock.add(timeElapsedAfterPut);

    // Then
    assertThat(patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, block))
        .isEqualTo(isExploitExpected);
  }

  private static @NotNull Stream<Arguments> whenPuttingTag() {
    return Stream.of(
        arguments(
            named("Being persistent and put less than 3 seconds before check", false),
            Duration.ofSeconds(1),
            true),
        arguments(
            named("Being persistent and put more than 3 seconds before check", false),
            Duration.ofSeconds(4),
            true),
        arguments(
            named("Being ephemeral and put less than 3 seconds before check", true),
            Duration.ofSeconds(1),
            true),
        arguments(
            named("Being ephemeral and put more than 3 seconds before check", true),
            Duration.ofSeconds(4),
            false));
  }

  @Test
  void whenMovingTag_shouldDetectExploitInNewLocationButNotInOldOne() {
    // Given
    BlockLocation oldBlockLocation = randomBlockLocation;
    Block oldBlock = new Block(randomBlockLocation, "STONE");
    patchPlaceBreakApi.putTag(oldBlock, true).join();

    Vector direction = new Vector(1, 0, 0);
    Set<Block> blocks = Collections.singleton(oldBlock);

    // When
    patchPlaceBreakApi.moveTags(blocks, direction).join();

    // Then
    BlockLocation newBlockLocation =
        new BlockLocation(
            "world", oldBlockLocation.x() + 1, oldBlockLocation.y(), oldBlockLocation.z());
    Block newBlock = new Block(newBlockLocation, "STONE");

    boolean isOnOldBlockAnExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, oldBlock);
    boolean isOnNewBlockAnExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, newBlock);

    assertAll(
        () -> assertThat(isOnOldBlockAnExploit).isFalse(),
        () -> assertThat(isOnNewBlockAnExploit).isTrue());
  }

  @Test
  void whenRemovingTag_shouldNotDetectExploit() {
    // Given
    Block block = new Block(randomBlockLocation, "STONE");
    patchPlaceBreakApi.putTag(block, false).join();

    // When
    patchPlaceBreakApi.removeTag(block).join();

    // Then
    boolean isExploit = patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, block);

    assertThat(isExploit).isFalse();
  }

  /* Helpers */

  /**
   * Prevents collisions by generating random block location for each test since for performances
   * purposes the database will not be recycled between tests.
   */
  private static @NotNull BlockLocation generateRandomBlockLocation() {
    Random random = new Random();
    int randX = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    int randY = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    int randZ = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    return new BlockLocation("world", randX, randY, randZ);
  }
}
