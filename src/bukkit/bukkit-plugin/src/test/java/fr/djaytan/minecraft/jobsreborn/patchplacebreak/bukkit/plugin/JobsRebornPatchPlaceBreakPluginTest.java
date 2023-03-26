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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.block.BlockStateMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;
import org.threeten.extra.MutableClock;

class JobsRebornPatchPlaceBreakPluginTest {

  private static final MutableClock mutableClock = MutableClock.epochUTC();
  private static PatchPlaceBreakBukkitAdapterApi patchApi;
  private static ServerMock serverMock;

  @BeforeAll
  static void beforeAll() {
    Awaitility.waitAtMost(1, TimeUnit.SECONDS);
    System.setProperty("bstats.relocatecheck", "false");
    serverMock = MockBukkit.mock();
    JobsRebornPatchPlaceBreakPlugin plugin =
        MockBukkit.load(JobsRebornPatchPlaceBreakPlugin.class, mutableClock);
    patchApi = plugin.patchPlaceBreakBukkitAdapterApi();
  }

  @AfterAll
  static void afterAll() {
    MockBukkit.unload();
  }

  @Test
  @DisplayName("With player breaking natural block")
  void withPlayerBreakingBlock() {
    // Given
    PlayerMock player = serverMock.addPlayer();
    WorldMock worldMock = serverMock.addSimpleWorld("world");
    Location nominalLocation = new Location(worldMock, 145, 70, 87.909);
    BlockMock blockMock = new BlockMock(Material.DIAMOND_ORE, nominalLocation);
    ActionInfo actionInfo = new BlockActionInfo(blockMock, ActionType.BREAK);

    Preconditions.condition(
        !patchApi.isPlaceAndBreakExploit(actionInfo, blockMock),
        "No tag is supposed to exist yet on the targeted block");

    // When
    boolean isBlockBroken = player.simulateBlockBreak(blockMock);

    // Then
    assertThat(isBlockBroken).isTrue();
    await().until(() -> patchApi.isPlaceAndBreakExploit(actionInfo, blockMock));
    mutableClock.add(5, ChronoUnit.SECONDS);
    assertThat(patchApi.isPlaceAndBreakExploit(actionInfo, blockMock)).isFalse();
  }

  @Test
  @DisplayName("With player placing block")
  void withPlayerPlacingBlock() {
    // Given
    PlayerMock playerMock = serverMock.addPlayer();
    WorldMock worldMock = serverMock.addSimpleWorld("world");
    Location nominalLocation = new Location(worldMock, 112.1, 64, 45.87);
    BlockMock blockToPlace = new BlockMock(Material.DIAMOND_ORE, nominalLocation);
    BlockStateMock blockStateMock = new BlockStateMock();
    ActionInfo actionInfo = new BlockActionInfo(blockToPlace, ActionType.BREAK);

    Preconditions.condition(
        !patchApi.isPlaceAndBreakExploit(actionInfo, blockToPlace),
        "No tag is supposed to exist yet on the targeted block");

    BlockMock replaceAgainstBlock = new BlockMock(Material.AIR, nominalLocation);
    ItemStack itemInHand = new ItemStack(Material.DIAMOND_ORE, 64);
    BlockPlaceEvent blockPlaceEvent =
        new BlockPlaceEvent(
            blockToPlace,
            blockStateMock,
            replaceAgainstBlock,
            itemInHand,
            playerMock,
            true,
            EquipmentSlot.HAND);

    // When
    serverMock.getPluginManager().callEvent(blockPlaceEvent);

    // Then
    await().until(() -> patchApi.isPlaceAndBreakExploit(actionInfo, blockToPlace));
  }
}
