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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.ActionTypeConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.BlockFaceConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.LocationConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.PatchPlaceBreakCore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class PatchPlaceBreakBukkitAdapterApiIntegrationTest {

  private static Path dataFolder;
  private static PatchPlaceBreakCore patchPlaceBreakCore;
  private static PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;

  @BeforeAll
  @SneakyThrows
  static void beforeAll() {
    dataFolder = Files.createTempDirectory("ppb-bukkit-api-it");
    ClassLoader classLoader = PatchPlaceBreakCore.class.getClassLoader();
    patchPlaceBreakCore = new PatchPlaceBreakCore();
    PatchPlaceBreakApi patchPlaceBreakApi = patchPlaceBreakCore.enable(classLoader, dataFolder);

    ActionTypeConverter actionTypeConverter = new ActionTypeConverter();
    BlockFaceConverter blockFaceConverter = new BlockFaceConverter();
    LocationConverter locationConverter = new LocationConverter();

    patchPlaceBreakBukkitAdapterApi =
        new PatchPlaceBreakBukkitAdapterApi(
            actionTypeConverter, blockFaceConverter, locationConverter, patchPlaceBreakApi);
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
  void whenPuttingTag_shouldNotThrow(@Mock Block block, @Mock World world) {
    // Given
    given(world.getName()).willReturn("world");
    given(block.getWorld()).willReturn(world);
    given(block.getX()).willReturn(12);
    given(block.getY()).willReturn(45);
    given(block.getZ()).willReturn(-234);
    given(block.getType()).willReturn(Material.STONE);
    boolean isEphemeral = false;

    // When
    ThrowingCallable throwingCallable =
        () -> patchPlaceBreakBukkitAdapterApi.putTag(block, isEphemeral).join();

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(2)
  @DisplayName("When checking exploit with existing tag")
  void whenCheckingExploit_withExistingTag_shouldDetectExploit(
      @Mock ActionInfo actionInfo, @Mock Block block, @Mock World world) {
    // Given
    given(actionInfo.getType()).willReturn(ActionType.BREAK);
    given(world.getName()).willReturn("world");
    given(block.getWorld()).willReturn(world);
    given(block.getX()).willReturn(12);
    given(block.getY()).willReturn(45);
    given(block.getZ()).willReturn(-234);
    given(block.getType()).willReturn(Material.STONE);

    // When
    boolean isExploit = patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(actionInfo, block);

    // Then
    assertThat(isExploit).isTrue();
  }

  @Test
  @Order(3)
  @DisplayName("When moving tag")
  void whenMovingTag_shouldNotThrow(@Mock Block block, @Mock World world) {
    // Given
    BlockFace blockFace = BlockFace.EAST;
    given(world.getName()).willReturn("world");
    given(block.getWorld()).willReturn(world);
    given(block.getX()).willReturn(12);
    given(block.getY()).willReturn(45);
    given(block.getZ()).willReturn(-234);
    given(block.getType()).willReturn(Material.STONE);
    Collection<Block> blocks = Collections.singleton(block);

    // When
    ThrowingCallable throwingCallable =
        () -> patchPlaceBreakBukkitAdapterApi.moveTags(blocks, blockFace).join();

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(4)
  @DisplayName("When checking exploit with moved tag")
  void whenCheckingExploit_withMovedTag_shouldDetectExploitInNewLocationButNotInOldOne(
      @Mock ActionInfo actionInfo, @Mock Block oldBlock, @Mock Block newBlock, @Mock World world) {
    // Given
    given(actionInfo.getType()).willReturn(ActionType.BREAK);
    given(world.getName()).willReturn("world");

    given(oldBlock.getWorld()).willReturn(world);
    given(oldBlock.getX()).willReturn(12);
    given(oldBlock.getY()).willReturn(45);
    given(oldBlock.getZ()).willReturn(-234);
    given(oldBlock.getType()).willReturn(Material.STONE);

    given(newBlock.getWorld()).willReturn(world);
    given(newBlock.getX()).willReturn(13);
    given(newBlock.getY()).willReturn(45);
    given(newBlock.getZ()).willReturn(-234);
    given(newBlock.getType()).willReturn(Material.STONE);

    // When
    boolean isOnOldBlockAnExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(actionInfo, oldBlock);
    boolean isOnNewBlockAnExploit =
        patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(actionInfo, newBlock);

    // Then
    assertAll(
        () -> assertThat(isOnOldBlockAnExploit).isFalse(),
        () -> assertThat(isOnNewBlockAnExploit).isTrue());
  }

  @Test
  @Order(5)
  @DisplayName("When removing tag")
  void whenRemovingTag_shouldNotThrow(@Mock Block block, @Mock World world) {
    // Given
    given(world.getName()).willReturn("world");
    given(block.getWorld()).willReturn(world);
    given(block.getX()).willReturn(13);
    given(block.getY()).willReturn(45);
    given(block.getZ()).willReturn(-234);
    given(block.getType()).willReturn(Material.STONE);

    // When
    ThrowingCallable throwingCallable =
        () -> patchPlaceBreakBukkitAdapterApi.removeTag(block).join();

    // Then
    assertThatCode(throwingCallable).doesNotThrowAnyException();
  }

  @Test
  @Order(6)
  @DisplayName("When checking exploit without existing tag")
  void whenCheckingExploit_withoutExistingTag_shouldNotDetectExploit(
      @Mock ActionInfo actionInfo, @Mock Block block, @Mock World world) {
    // Given
    given(actionInfo.getType()).willReturn(ActionType.BREAK);
    given(world.getName()).willReturn("world");
    given(block.getWorld()).willReturn(world);
    given(block.getX()).willReturn(13);
    given(block.getY()).willReturn(45);
    given(block.getZ()).willReturn(-234);
    given(block.getType()).willReturn(Material.STONE);

    // When
    boolean isExploit = patchPlaceBreakBukkitAdapterApi.isPlaceAndBreakExploit(actionInfo, block);

    // Then
    assertThat(isExploit).isFalse();
  }
}
