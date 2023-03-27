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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.JobsRebornPatchPlaceBreakPlugin;
import java.time.Clock;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.Preconditions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatchPlaceBreakVerifierTest {

  private static PatchPlaceBreakBukkitAdapterApi patchApi;

  @Mock private Job job;
  @Mock private ListenerRegister listenerRegister;
  @Mock private Player player;
  private PatchPlaceBreakVerifier patchPlaceBreakVerifier;

  @BeforeAll
  static void beforeAll() {
    System.setProperty("bstats.relocatecheck", "false");
    MockBukkit.mock();
    JobsRebornPatchPlaceBreakPlugin plugin =
        MockBukkit.load(JobsRebornPatchPlaceBreakPlugin.class, Clock.systemUTC());
    patchApi = plugin.patchPlaceBreakBukkitAdapterApi();
  }

  @BeforeEach
  void beforeEach() {
    patchPlaceBreakVerifier = new PatchPlaceBreakVerifier(() -> listenerRegister, patchApi);
  }

  @AfterAll
  static void afterAll() {
    MockBukkit.unload();
  }

  @ParameterizedTest(name = "{index} - And applied: {0}")
  @ValueSource(booleans = {true, false})
  @DisplayName("When verifying whereas patch is unexpected")
  void whenVerifyingWhereasPatchIsUnexpected(boolean isPatchApplied) {
    // Given
    BukkitPatchEnvironmentState environmentState = createEnvironmentState(isPatchApplied);

    ActionInfo actionInfo = environmentState.getJobActionInfo();
    Block block = environmentState.getTargetedBlock();
    Preconditions.condition(
        !patchApi.isPlaceAndBreakExploit(actionInfo, block), "Patch is unexpected.");

    // When
    patchPlaceBreakVerifier.checkAndAttemptFixListenersIfRequired(environmentState).join();

    // Then
    verifyNoInteractions(listenerRegister);
  }

  @Test
  @DisplayName("When verifying with patch expected and applied")
  void whenVerifyingWithPatchExpectedAndApplied() {
    // Given
    boolean isPatchApplied = true;
    BukkitPatchEnvironmentState environmentState = createEnvironmentState(isPatchApplied);

    ActionInfo actionInfo = environmentState.getJobActionInfo();
    Block block = environmentState.getTargetedBlock();
    patchApi.putTag(block, false).join();

    Preconditions.condition(
        patchApi.isPlaceAndBreakExploit(actionInfo, block), "Patch is expected.");

    // When
    patchPlaceBreakVerifier.checkAndAttemptFixListenersIfRequired(environmentState).join();

    // Then
    verifyNoInteractions(listenerRegister);
  }

  @Test
  @DisplayName("When verifying with patch expected but not applied")
  void whenVerifyingWithPatchExpectedButNotApplied() {
    // Given
    boolean isPatchApplied = false;
    BukkitPatchEnvironmentState environmentState = createEnvironmentState(isPatchApplied);

    ActionInfo actionInfo = environmentState.getJobActionInfo();
    Block block = environmentState.getTargetedBlock();
    patchApi.putTag(block, false).join();

    Preconditions.condition(
        patchApi.isPlaceAndBreakExploit(actionInfo, block), "Patch is expected.");

    // When
    patchPlaceBreakVerifier.checkAndAttemptFixListenersIfRequired(environmentState).join();

    // Then
    verify(listenerRegister).reloadListeners();
  }

  /* Helper methods */

  private @NotNull BukkitPatchEnvironmentState createEnvironmentState(boolean isPatchApplied) {
    Location location = createRandomLocation();
    Block block = new BlockMock(Material.ENDER_STONE, location);
    ActionInfo actionInfo = new BlockActionInfo(block, ActionType.BREAK);
    Event event = new BlockBreakEvent(block, player);
    HandlerList handlerList = event.getHandlers();

    return new BukkitPatchEnvironmentState(
        actionInfo, block, player, job, event, isPatchApplied, handlerList);
  }

  private @NotNull Location createRandomLocation() {
    World world = new WorldMock();
    int randX = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();
    int randY = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();
    int randZ = (RandomUtils.nextBoolean() ? 1 : -1) * RandomUtils.nextInt();
    return new Location(world, randX, randY, randZ);
  }
}
