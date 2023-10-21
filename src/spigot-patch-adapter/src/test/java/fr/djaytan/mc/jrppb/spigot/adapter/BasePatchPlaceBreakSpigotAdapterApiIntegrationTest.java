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
package fr.djaytan.mc.jrppb.spigot.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.mc.jrppb.api.PatchPlaceBreakApi;
import fr.djaytan.mc.jrppb.core.PatchPlaceBreakCore;
import fr.djaytan.mc.jrppb.spigot.adapter.converter.ActionTypeConverter;
import fr.djaytan.mc.jrppb.spigot.adapter.converter.BlockFaceConverter;
import fr.djaytan.mc.jrppb.spigot.adapter.converter.LocationConverter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
abstract class BasePatchPlaceBreakSpigotAdapterApiIntegrationTest {

  private final MutableClock mutableClock = MutableClock.epochUTC();
  private final PatchPlaceBreakCore patchPlaceBreakCore = new PatchPlaceBreakCore();
  private PatchPlaceBreakSpigotAdapterApi patchPlaceBreakSpigotAdapterApi;
  @TempDir protected Path dataFolder;

  @Mock(strictness = Strictness.LENIENT)
  private Block randomBlockMocked;

  @Mock(strictness = Strictness.LENIENT)
  private World worldMocked;

  @BeforeEach
  void beforeEach() throws IOException {
    patchPlaceBreakSpigotAdapterApi = createPatchPlaceBreakSpigotAdapterApi();
    prepareRandomBlockMocked(randomBlockMocked, worldMocked);
  }

  @AfterEach
  void afterEach() {
    patchPlaceBreakCore.disable();
  }

  /**
   * If the block is not specified, then we consider that the given action doesn't target any
   * particular block. Hence, we consider the action is not an exploit one.
   */
  @Test
  void whenCheckingExploitWhileActionInfoIsNull_shouldNotDetectExploit() {
    // Given

    // When
    boolean isExploit =
        patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(null, randomBlockMocked);

    // Then
    assertThat(isExploit).isFalse();
  }

  /**
   * If the block is not specified, then we consider that the given action doesn't target any
   * particular block. Hence, we consider the action is not an exploit one.
   */
  @Test
  void whenCheckingExploitWhileBlockIsNull_shouldNotDetectExploit(@Mock ActionInfo actionInfo) {
    // Given

    // When
    boolean isExploit = patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(actionInfo, null);

    // Then
    assertThat(isExploit).isFalse();
  }

  @Test
  void whenTagDoesntExist_shouldNotDetectExploit() {
    // Given
    ActionInfo actionInfo = new BlockActionInfo(randomBlockMocked, ActionType.PLACE);
    given(randomBlockMocked.getType()).willReturn(Material.STONE);

    // When
    boolean isExploit =
        patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(actionInfo, randomBlockMocked);

    // Then
    assertThat(isExploit).isFalse();
  }

  @Test
  void whenActionIsBlacklisted_shouldNotDetectExploit() {
    // Given
    ActionInfo actionInfo = new BlockActionInfo(randomBlockMocked, ActionType.BREAK);
    given(randomBlockMocked.getType()).willReturn(Material.AIR);

    // When
    boolean isExploit =
        patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(actionInfo, randomBlockMocked);

    // Then
    assertThat(isExploit).isFalse();
  }

  @ParameterizedTest
  @MethodSource
  void whenPuttingTag(
      boolean isEphemeral, @NotNull TemporalAmount timeElapsedAfterPut, boolean isExploitExpected) {
    // Given
    ActionInfo actionInfo = new BlockActionInfo(randomBlockMocked, ActionType.PLACE);

    // When
    patchPlaceBreakSpigotAdapterApi.putTag(randomBlockMocked, isEphemeral).join();
    mutableClock.add(timeElapsedAfterPut);

    // Then
    boolean isExploit =
        patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(actionInfo, randomBlockMocked);

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
  void whenMovingTag_shouldDetectExploitInNewLocationButNotInOldOne(@Mock @NotNull Block newBlock) {
    // Given
    Block oldBlock = randomBlockMocked;
    patchPlaceBreakSpigotAdapterApi.putTag(oldBlock, true).join();

    BlockFace blockFace = BlockFace.EAST;
    Set<Block> blocks = Collections.singleton(oldBlock);

    // When
    patchPlaceBreakSpigotAdapterApi.moveTags(blocks, blockFace).join();

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

    ActionInfo actionInfo = new BlockActionInfo(randomBlockMocked, ActionType.PLACE);

    boolean isOnOldBlockAnExploit =
        patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(actionInfo, oldBlock);
    boolean isOnNewBlockAnExploit =
        patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(actionInfo, newBlock);

    assertAll(
        () -> assertThat(isOnOldBlockAnExploit).isFalse(),
        () -> assertThat(isOnNewBlockAnExploit).isTrue());
  }

  @Test
  void whenRemovingTag_shouldThenNotDetectExploit() {
    // Given
    patchPlaceBreakSpigotAdapterApi.putTag(randomBlockMocked, false).join();

    // When
    patchPlaceBreakSpigotAdapterApi.removeTag(randomBlockMocked).join();

    // Then
    ActionInfo actionInfo = new BlockActionInfo(randomBlockMocked, ActionType.PLACE);

    boolean isExploit =
        patchPlaceBreakSpigotAdapterApi.isPlaceAndBreakExploit(actionInfo, randomBlockMocked);

    assertThat(isExploit).isFalse();
  }

  /* Helpers */

  private @NotNull PatchPlaceBreakSpigotAdapterApi createPatchPlaceBreakSpigotAdapterApi() {
    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();
    PatchPlaceBreakApi patchPlaceBreakApi =
        patchPlaceBreakCore.enable(classLoader, mutableClock, dataFolder);

    ActionTypeConverter actionTypeConverter = new ActionTypeConverter();
    BlockFaceConverter blockFaceConverter = new BlockFaceConverter();
    LocationConverter locationConverter = new LocationConverter();

    return new PatchPlaceBreakSpigotAdapterApi(
        actionTypeConverter, blockFaceConverter, locationConverter, patchPlaceBreakApi);
  }

  /**
   * Prevents collisions by generating random block for each test since for performances purposes
   * the database will not be recycled between tests.
   */
  private static void prepareRandomBlockMocked(@NotNull Block blockMocked, @NotNull World world) {
    Random random = new Random();
    int randX = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    int randY = (random.nextBoolean() ? 1 : -1) * random.nextInt();
    int randZ = (random.nextBoolean() ? 1 : -1) * random.nextInt();

    when(world.getName()).thenReturn("world");
    when(blockMocked.getWorld()).thenReturn(world);
    when(blockMocked.getX()).thenReturn(randX);
    when(blockMocked.getY()).thenReturn(randY);
    when(blockMocked.getZ()).thenReturn(randZ);
    when(blockMocked.getType()).thenReturn(Material.STONE);
  }
}
