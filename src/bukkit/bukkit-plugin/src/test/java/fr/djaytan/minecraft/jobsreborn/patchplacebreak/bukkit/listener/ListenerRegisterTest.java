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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockBreakListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockGrowListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockPistonListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockPlaceListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block.BlockSpreadListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsExpGainListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsPrePaymentListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.JobsRebornPatchPlaceBreakPlugin;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;

class ListenerRegisterTest {

  private static JavaPlugin plugin;
  private static PatchPlaceBreakBukkitAdapterApi patchApi;
  private static ServerMock serverMock;
  private ListenerRegister listenerRegister;

  @BeforeAll
  static void beforeAll() {
    Awaitility.waitAtMost(1, TimeUnit.SECONDS);
    System.setProperty("bstats.relocatecheck", "false");
    serverMock = MockBukkit.mock();
    JobsRebornPatchPlaceBreakPlugin jobsRebornPatchPlaceBreakPlugin =
        MockBukkit.load(JobsRebornPatchPlaceBreakPlugin.class, Clock.systemUTC());
    plugin = jobsRebornPatchPlaceBreakPlugin;
    patchApi = jobsRebornPatchPlaceBreakPlugin.patchPlaceBreakBukkitAdapterApi();
  }

  @BeforeEach
  void beforeEach() {
    BlockBreakListener blockBreakListener = new BlockBreakListener(patchApi);
    BlockGrowListener blockGrowListener = new BlockGrowListener(patchApi);
    BlockPistonListener blockPistonListener = new BlockPistonListener(patchApi);
    BlockPlaceListener blockPlaceListener = new BlockPlaceListener(patchApi);
    BlockSpreadListener blockSpreadListener = new BlockSpreadListener(patchApi);
    JobsExpGainListener jobsExpGainListener = new JobsExpGainListener(patchApi);
    JobsPrePaymentListener jobsPrePaymentListener = new JobsPrePaymentListener(patchApi);
    listenerRegister =
        new ListenerRegister(
            plugin,
            plugin.getServer().getPluginManager(),
            blockBreakListener,
            blockGrowListener,
            blockPistonListener,
            blockPlaceListener,
            blockSpreadListener,
            jobsExpGainListener,
            jobsPrePaymentListener);
  }

  @AfterAll
  static void afterAll() {
    MockBukkit.unload();
  }

  @Test
  @DisplayName("When registering listeners")
  void whenRegisteringListeners() {
    // Given
    WorldMock worldMock = serverMock.addSimpleWorld("world");
    Location location = new Location(worldMock, 47, 64, -87);
    BlockMock blockMock = new BlockMock(Material.STONE, location);
    PlayerMock playerMock = serverMock.addPlayer();
    ActionInfo actionInfo = new BlockActionInfo(blockMock, ActionType.PLACE);

    Preconditions.condition(
        !patchApi.isPlaceAndBreakExploit(actionInfo, blockMock),
        "No tag is supposed to exist yet on the targeted block");

    // When
    listenerRegister.registerListeners();

    // Then
    BlockPlaceEvent blockPlaceEvent =
        new BlockPlaceEvent(blockMock, null, null, null, playerMock, true, null);
    serverMock.getPluginManager().callEvent(blockPlaceEvent);

    assertThat(blockPlaceEvent.isCancelled()).isFalse();
    await()
        .pollDelay(Duration.ofSeconds(1))
        .until(() -> patchApi.isPlaceAndBreakExploit(actionInfo, blockMock));
  }
}
