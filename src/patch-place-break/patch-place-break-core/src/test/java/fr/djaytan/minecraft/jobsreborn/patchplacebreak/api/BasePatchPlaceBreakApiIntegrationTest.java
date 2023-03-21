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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakCore;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.threeten.extra.MutableClock;

abstract class BasePatchPlaceBreakApiIntegrationTest {

  private final BlockLocation randomBlockLocation = generateRandomBlockLocation();
  private final MutableClock mutableClock = MutableClock.epochUTC();
  private PatchPlaceBreakApi patchPlaceBreakApi;
  private final PatchPlaceBreakCore patchPlaceBreakCore = new PatchPlaceBreakCore();
  @TempDir protected Path dataFolder;

  @BeforeEach
  @SneakyThrows
  void beforeEach() {
    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();
    patchPlaceBreakApi = patchPlaceBreakCore.enable(classLoader, mutableClock, dataFolder);
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    patchPlaceBreakCore.disable();
  }

  @Test
  @DisplayName("When tag doesn't exist")
  void whenTagDoesntExist_shouldNotDetectExploit() {
    // Given

    // When
    boolean isExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, randomBlockLocation);

    // Then
    assertThat(isExploit).isFalse();
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("When putting tag")
  void whenPuttingTag(
      boolean isEphemeral, @NotNull TemporalAmount timeElapsedAfterPut, boolean isExploitExpected) {
    // Given

    // When
    patchPlaceBreakApi.putTag(randomBlockLocation, isEphemeral).join();
    mutableClock.add(timeElapsedAfterPut);

    // Then
    boolean isExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, randomBlockLocation);

    assertThat(isExploit).isEqualTo(isExploitExpected);
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
  @DisplayName("When moving tag")
  void whenMovingTag_shouldDetectExploitInNewLocationButNotInOldOne() {
    // Given
    BlockLocation oldBlockLocation = randomBlockLocation;
    patchPlaceBreakApi.putTag(oldBlockLocation, true).join();

    Vector direction = Vector.of(1, 0, 0);
    Set<BlockLocation> blocks = Collections.singleton(oldBlockLocation);

    // When
    patchPlaceBreakApi.moveTags(blocks, direction).join();

    // Then
    BlockLocation newBlockLocation =
        BlockLocation.of(
            "world", oldBlockLocation.getX() + 1, oldBlockLocation.getY(), oldBlockLocation.getZ());

    boolean isOnOldBlockAnExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, oldBlockLocation);
    boolean isOnNewBlockAnExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, newBlockLocation);

    assertAll(
        () -> assertThat(isOnOldBlockAnExploit).isFalse(),
        () -> assertThat(isOnNewBlockAnExploit).isTrue());
  }

  @Test
  @DisplayName("When removing tag")
  void whenRemovingTag_shouldThenNotDetectExploit() {
    // Given
    patchPlaceBreakApi.putTag(randomBlockLocation, false).join();

    // When
    patchPlaceBreakApi.removeTag(randomBlockLocation).join();

    // Then
    boolean isExploit =
        patchPlaceBreakApi.isPlaceAndBreakExploit(BlockActionType.BREAK, randomBlockLocation);

    assertThat(isExploit).isFalse();
  }

  /*
   * Helper methods
   */

  /**
   * Prevents collisions by generating random block location for each test since for performances
   * purpose the database will not be recycled between tests.
   */
  private static @NotNull BlockLocation generateRandomBlockLocation() {
    int randX = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();
    int randY = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();
    int randZ = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();

    return BlockLocation.of("world", randX, randY, randZ);
  }
}
