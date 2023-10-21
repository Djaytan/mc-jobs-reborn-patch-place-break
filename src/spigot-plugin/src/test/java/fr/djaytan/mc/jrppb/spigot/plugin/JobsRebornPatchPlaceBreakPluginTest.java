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
package fr.djaytan.mc.jrppb.spigot.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.mc.jrppb.spigot.adapter.PatchPlaceBreakSpigotAdapterApi;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.threeten.extra.MutableClock;

class JobsRebornPatchPlaceBreakPluginTest {

  private static final MutableClock mutableClock = MutableClock.epochUTC();
  private static PatchPlaceBreakSpigotAdapterApi patchApi;
  private static ServerMock serverMock;

  @BeforeAll
  static void beforeAll() {
    Awaitility.waitAtMost(1, TimeUnit.SECONDS);
    System.setProperty("bstats.relocatecheck", "false");
    serverMock = MockBukkit.mock();
    JobsRebornPatchPlaceBreakPlugin plugin =
        MockBukkit.load(JobsRebornPatchPlaceBreakPlugin.class, mutableClock);
    patchApi = plugin.patchPlaceBreakSpigotAdapterApi();
  }

  @AfterAll
  static void afterAll() {
    MockBukkit.unmock();
  }

  /** Obviously relevant against place-break diamond ore exploit. */
  @Test
  void whenPlayerPlaceDiamondOre_shouldDetectExploitIfBroken() {
    // Given
    PlayerMock playerMock = serverMock.addPlayer();
    WorldMock worldMock = serverMock.addSimpleWorld("world");
    Location nominalLocation = new Location(worldMock, 112.1, 64, 45.87);
    BlockMock blockToPlace = new BlockMock(Material.DIAMOND_ORE, nominalLocation);
    ActionInfo actionInfo = new BlockActionInfo(blockToPlace, ActionType.BREAK);

    BlockPlaceEvent blockPlaceEvent =
        new BlockPlaceEvent(blockToPlace, null, null, null, playerMock, true, EquipmentSlot.HAND);

    // When
    serverMock.getPluginManager().callEvent(blockPlaceEvent);

    // Then
    await().until(() -> patchApi.isPlaceAndBreakExploit(actionInfo, blockToPlace));
  }

  /**
   * Typically, this is relevant against place-break of saplings when profession pay for placing
   * them.
   */
  @Test
  void whenPlayerBreakSapling_shouldDetectExploitIfPutBackQuickly() {
    // Given
    PlayerMock player = serverMock.addPlayer();
    WorldMock worldMock = serverMock.addSimpleWorld("world");
    Location nominalLocation = new Location(worldMock, 145, 70, 87.909);
    BlockMock blockMock = new BlockMock(Material.OAK_SAPLING, nominalLocation);

    // When
    BlockBreakEvent blockBreakEvent = player.simulateBlockBreak(blockMock);

    // Then
    assertThat(blockBreakEvent).isNotNull();
    assertAll(
        () -> assertThat(blockBreakEvent.isCancelled()).isFalse(),
        () -> assertThat(blockMock.getType()).isEqualTo(Material.AIR));

    ActionInfo actionInfo = new BlockActionInfo(blockMock, ActionType.PLACE);
    await().until(() -> patchApi.isPlaceAndBreakExploit(actionInfo, blockMock));
    mutableClock.add(3, ChronoUnit.SECONDS);
    assertThat(patchApi.isPlaceAndBreakExploit(actionInfo, blockMock)).isFalse();
  }

  /**
   * That's weird that JobsReborn behaves like that, but it is the case, so unfortunately we need to
   * test the patch under these conditions.
   */
  @Test
  void whenJobsRebornThinksAirBlockHasBeenBroken_shouldNotConsiderItAsAnExploit() {
    // Given
    WorldMock worldMock = serverMock.addSimpleWorld("world");
    Location nominalLocation = new Location(worldMock, 5869.25, 72, 457.01);
    BlockMock airBlockToBreak = new BlockMock(Material.AIR, nominalLocation);
    ActionInfo actionInfo = new BlockActionInfo(airBlockToBreak, ActionType.BREAK);

    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(airBlockToBreak, null);

    // When
    serverMock.getPluginManager().callEvent(blockBreakEvent);

    // Then
    await()
        .pollDelay(Duration.ofSeconds(2)) // Lets event effect propagate
        .atMost(Duration.ofSeconds(3))
        .until(() -> !patchApi.isPlaceAndBreakExploit(actionInfo, airBlockToBreak));
  }

  /**
   * That's weird that JobsReborn behaves like that, but it is the case, so unfortunately we need to
   * test the patch under these conditions.
   */
  @Test
  void whenJobsRebornThinksWaterBlockHasBeenBroken_shouldNotConsiderItAsAnExploit() {
    // Given
    WorldMock worldMock = serverMock.addSimpleWorld("world");
    Location nominalLocation = new Location(worldMock, 5414.6, 74.5, 449.1);
    BlockMock waterBlockToBreak = new BlockMock(Material.WATER, nominalLocation);
    ActionInfo actionInfo = new BlockActionInfo(waterBlockToBreak, ActionType.BREAK);

    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(waterBlockToBreak, null);

    // When
    serverMock.getPluginManager().callEvent(blockBreakEvent);

    // Then
    await()
        .pollDelay(Duration.ofSeconds(2)) // Lets event effect propagate
        .atMost(Duration.ofSeconds(3))
        .until(() -> !patchApi.isPlaceAndBreakExploit(actionInfo, waterBlockToBreak));
  }
}
