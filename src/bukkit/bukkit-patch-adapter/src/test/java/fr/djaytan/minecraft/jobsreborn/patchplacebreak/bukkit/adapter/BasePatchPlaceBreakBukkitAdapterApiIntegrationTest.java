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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.ActionTypeConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.BlockFaceConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.LocationConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakCore;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.threeten.extra.MutableClock;

@ExtendWith(MockitoExtension.class)
abstract class BasePatchPlaceBreakBukkitAdapterApiIntegrationTest {

  private final MutableClock mutableClock = MutableClock.epochUTC();
  private final PatchPlaceBreakCore patchPlaceBreakCore = new PatchPlaceBreakCore();
  private PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;
  @TempDir protected Path dataFolder;

  @Mock(strictness = Strictness.LENIENT)
  private ActionInfo sampleActionInfoMocked;

  @Mock(strictness = Strictness.LENIENT)
  private Block randomBlockMocked;

  @Mock(strictness = Strictness.LENIENT)
  private World worldMocked;

  @BeforeEach
  @SneakyThrows
  void beforeEach() {
    patchPlaceBreakBukkitAdapterApi = createPatchPlaceBreakBukkitAdapterApi();
    prepareSampleActionInfoMocked(sampleActionInfoMocked);
    prepareRandomBlockMocked(randomBlockMocked, worldMocked);
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    patchPlaceBreakCore.disable();
  }

  /**
   * If the action is not specified then we consider that no particular action has been performed on
   * the targeted block. Hence, we consider the action is not an exploit one.
   */
  @Test
  @DisplayName("When checking exploit while 'actionInfo' is null")
  void whenCheckingExploitWhileActionInfoIsNull_shouldNotDetectExploit() {
    // Given
    ActionInfo actionInfo = null;

    // When
    boolean isExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(actionInfo, randomBlockMocked);

    // Then
    assertThat(isExploit).isFalse();
  }

  /**
   * If the block is not specified then we consider that no particular block is targeted by the
   * given action. Hence, we consider the action is not an exploit one.
   */
  @Test
  @DisplayName("When checking exploit while 'block' is null")
  void whenCheckingExploitWhileBlockIsNull_shouldNotDetectExploit() {
    // Given
    Block block = null;

    // When
    boolean isExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(sampleActionInfoMocked, block);

    // Then
    assertThat(isExploit).isFalse();
  }

  @Test
  @DisplayName("When tag doesn't exist")
  void whenTagDoesntExist_shouldNotDetectExploit() {
    // Given

    // When
    boolean isExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
            sampleActionInfoMocked, randomBlockMocked);

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
    patchPlaceBreakBukkitAdapterApi.putTag(randomBlockMocked, isEphemeral).join();
    mutableClock.add(timeElapsedAfterPut);

    // Then
    boolean isExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
            sampleActionInfoMocked, randomBlockMocked);

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
  void whenMovingTag_shouldDetectExploitInNewLocationButNotInOldOne(@Mock @NotNull Block newBlock) {
    // Given
    Block oldBlock = randomBlockMocked;
    patchPlaceBreakBukkitAdapterApi.putTag(oldBlock, true).join();

    BlockFace blockFace = BlockFace.EAST;
    Set<Block> blocks = Collections.singleton(oldBlock);

    // When
    patchPlaceBreakBukkitAdapterApi.moveTags(blocks, blockFace).join();

    // Then
    World oldWorld = oldBlock.getWorld();
    int oldX = oldBlock.getX();
    int oldY = oldBlock.getY();
    int oldZ = oldBlock.getZ();

    when(newBlock.getWorld()).thenReturn(oldWorld);
    when(newBlock.getX()).thenReturn(oldX + 1);
    when(newBlock.getY()).thenReturn(oldY);
    when(newBlock.getZ()).thenReturn(oldZ);
    when(newBlock.getType()).thenReturn(Material.STONE);

    boolean isOnOldBlockAnExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(sampleActionInfoMocked, oldBlock);
    boolean isOnNewBlockAnExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(sampleActionInfoMocked, newBlock);

    assertAll(
        () -> assertThat(isOnOldBlockAnExploit).isFalse(),
        () -> assertThat(isOnNewBlockAnExploit).isTrue());
  }

  @Test
  @DisplayName("When removing tag")
  void whenRemovingTag_shouldThenNotDetectExploit() {
    // Given
    patchPlaceBreakBukkitAdapterApi.putTag(randomBlockMocked, false).join();

    // When
    patchPlaceBreakBukkitAdapterApi.removeTag(randomBlockMocked).join();

    // Then
    boolean isExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(
            sampleActionInfoMocked, randomBlockMocked);

    assertThat(isExploit).isFalse();
  }

  /* Helpers */

  private @NotNull PatchPlaceBreakBukkitAdapterApi createPatchPlaceBreakBukkitAdapterApi() {
    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();
    PatchPlaceBreakApi patchPlaceBreakApi =
        patchPlaceBreakCore.enable(classLoader, mutableClock, dataFolder);

    ActionTypeConverter actionTypeConverter = new ActionTypeConverter();
    BlockFaceConverter blockFaceConverter = new BlockFaceConverter();
    LocationConverter locationConverter = new LocationConverter();

    return new PatchPlaceBreakBukkitAdapterApi(
        actionTypeConverter, blockFaceConverter, locationConverter, patchPlaceBreakApi);
  }

  private static void prepareSampleActionInfoMocked(@NotNull ActionInfo actionInfoMocked) {
    when(actionInfoMocked.getType()).thenReturn(ActionType.BREAK);
  }

  /**
   * Prevents collisions by generating random block for each test since for performances purpose the
   * database will not be recycled between tests.
   */
  private static void prepareRandomBlockMocked(@NotNull Block blockMocked, @NotNull World world) {
    int randX = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();
    int randY = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();
    int randZ = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();

    when(world.getName()).thenReturn("world");
    when(blockMocked.getWorld()).thenReturn(world);
    when(blockMocked.getX()).thenReturn(randX);
    when(blockMocked.getY()).thenReturn(randY);
    when(blockMocked.getZ()).thenReturn(randZ);
    when(blockMocked.getType()).thenReturn(Material.STONE);
  }
}
