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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsPrePaymentListener;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.JobsRebornPatchPlaceBreakPlugin;
import java.time.Clock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BukkitPatchEnvironmentStateTest {

  private static JavaPlugin plugin;
  @Mock private Job job;
  @Mock private Player player;

  @BeforeAll
  static void beforeAll() {
    System.setProperty("bstats.relocatecheck", "false");
    MockBukkit.mock();
    plugin = MockBukkit.load(JobsRebornPatchPlaceBreakPlugin.class, Clock.systemUTC());
  }

  @AfterAll
  static void afterAll() {
    MockBukkit.unload();
  }

  @Test
  @DisplayName("When instantiating with nominal values")
  void whenInstantiatingWithNominalValues(@Mock Block block) {
    // Given
    ActionInfo actionInfo = new BlockActionInfo(block, ActionType.BREAK);
    Event event = new BlockBreakEvent(block, player);
    boolean isCancelled = false;
    HandlerList handlerList = event.getHandlers();

    // When
    BukkitPatchEnvironmentState environmentState =
        new BukkitPatchEnvironmentState(
            actionInfo, block, player, job, event, isCancelled, handlerList);

    // Then
    assertAll(
        () -> assertThat(environmentState.getJobActionInfo()).isSameAs(actionInfo),
        () -> assertThat(environmentState.getTargetedBlock()).isSameAs(block),
        () -> assertThat(environmentState.getInvolvedPlayer()).isSameAs(player),
        () -> assertThat(environmentState.getTriggeredJob()).isSameAs(job),
        () -> assertThat(environmentState.getEventHandled()).isSameAs(event),
        () -> assertThat(environmentState.isEventCancelled()).isSameAs(isCancelled),
        () -> assertThat(environmentState.getEventHandlers()).isSameAs(handlerList));
  }

  @Test
  @DisplayName("When calling toString() method")
  void whenCallingToStringMethod(
      @Mock JobsPrePaymentListener listener, @Mock RegisteredListener registeredListener) {
    // Given
    World world = new WorldMock();
    Location location = new Location(world, 15, 64, -78.05);
    Block block = new BlockMock(Material.ENDER_STONE, location);
    ActionInfo actionInfo = new BlockActionInfo(block, ActionType.BREAK);
    Event event = new BlockBreakEvent(block, player);
    boolean isCancelled = false;

    HandlerList handlerList = new HandlerList();

    given(registeredListener.getListener()).willReturn(listener);
    given(registeredListener.getPlugin()).willReturn(plugin);
    given(registeredListener.getPriority()).willReturn(EventPriority.HIGH);

    handlerList.register(registeredListener);

    given(player.getName()).willReturn("Bob");
    given(job.getName()).willReturn("Miner");

    // When
    BukkitPatchEnvironmentState environmentState =
        new BukkitPatchEnvironmentState(
            actionInfo, block, player, job, event, isCancelled, handlerList);
    String toStringResult = environmentState.toString();

    // Then
    assertThat(toStringResult)
        .containsPattern(
            "fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.BukkitPatchEnvironmentState@.*\\["
                + "jobActionInfo=BREAK,"
                + "targetedBlock=ENDER_STONE,"
                + "targetedLocation=Location\\{"
                + "world=be.seeseemelk.mockbukkit.WorldMock@.*,x=15.0,y=64.0,z=-78.05,pitch=0.0,yaw=0.0"
                + "},"
                + "involvedPlayer=Bob,"
                + "triggeredJob=Miner,"
                + "eventHandled=org.bukkit.event.block.BlockBreakEvent@.*,"
                + "isEventCancelled=false,"
                + "eventHandlers=org.bukkit.event.HandlerList@.*\\["
                + "registeredListeners=\\["
                + "org.bukkit.plugin.RegisteredListener@.*\\["
                + "listenerClass=fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.jobs.JobsPrePaymentListener,"
                + "pluginName=JobsReborn-PatchPlaceBreak,eventPriority=HIGH"
                + "]]]]");
  }
}
