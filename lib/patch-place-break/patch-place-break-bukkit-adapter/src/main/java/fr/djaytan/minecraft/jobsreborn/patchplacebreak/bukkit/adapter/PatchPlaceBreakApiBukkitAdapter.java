/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagVector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.ActionTypeConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.BlockFaceConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.LocationConverter;

@Singleton
public class PatchPlaceBreakApiBukkitAdapter {

  private final ActionTypeConverter actionTypeConverter;
  private final LocationConverter locationConverter;
  private final PatchPlaceBreakApi patchPlaceBreakApi;
  private final BlockFaceConverter blockFaceConverter;

  @Inject
  public PatchPlaceBreakApiBukkitAdapter(@NotNull ActionTypeConverter actionTypeConverter,
      @NotNull LocationConverter locationConverter, @NotNull PatchPlaceBreakApi patchPlaceBreakApi,
      @NotNull BlockFaceConverter blockFaceConverter) {
    this.actionTypeConverter = actionTypeConverter;
    this.locationConverter = locationConverter;
    this.patchPlaceBreakApi = patchPlaceBreakApi;
    this.blockFaceConverter = blockFaceConverter;
  }

  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> putTag(@NotNull Location location, boolean isEphemeral) {
    TagLocation tagLocation = locationConverter.convert(location);
    return patchPlaceBreakApi.putTag(tagLocation, isEphemeral);
  }

  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> putBackTagOnMovedBlocks(@NotNull List<Block> blocks,
      @NotNull BlockFace blockFace) {
    List<TagLocation> tagLocations = blocks.stream().map(Block::getLocation)
        .map(locationConverter::convert).collect(Collectors.toList());
    TagVector tagVector = blockFaceConverter.convert(blockFace);
    return patchPlaceBreakApi.putBackTagOnMovedBlocks(tagLocations, tagVector);
  }

  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> removeTag(@NotNull Location location) {
    TagLocation tagLocation = locationConverter.convert(location);
    return patchPlaceBreakApi.removeTag(tagLocation);
  }

  public @NotNull CompletableFuture<Boolean> isPlaceAndBreakAction(@NotNull ActionType actionType,
      @NotNull Location location) {
    PatchActionType patchActionType = actionTypeConverter.convert(actionType);
    TagLocation tagLocation = locationConverter.convert(location);
    return patchPlaceBreakApi.isPlaceAndBreakAction(patchActionType, tagLocation);
  }

  @CanIgnoreReturnValue
  public @NotNull CompletableFuture<Void> verifyPatchApplication(@NotNull ActionType jobActionType,
      @NotNull Block block, boolean isEventCancelled, @NotNull OfflinePlayer player,
      @NotNull Job job, @NotNull HandlerList handlerList) {
    Objects.requireNonNull(jobActionType);
    Objects.requireNonNull(block);
    Objects.requireNonNull(player);
    Objects.requireNonNull(job);
    Objects.requireNonNull(handlerList);

    Location blockLocation = block.getLocation();

    PatchActionType patchActionType = actionTypeConverter.convert(jobActionType);
    TagLocation tagLocation = locationConverter.convert(blockLocation);
    String blockTypeName = block.getType().name();
    String playerName = player.getName();
    String jobName = job.getName();
    List<String> detectedPotentialConflictingPluginsNames = getHandlerPluginsNames(handlerList);

    return patchPlaceBreakApi.verifyPatchApplication(patchActionType, tagLocation, blockTypeName,
        isEventCancelled, playerName, jobName, detectedPotentialConflictingPluginsNames);
  }

  private @NotNull List<String> getHandlerPluginsNames(@NotNull HandlerList handlerList) {
    return Arrays.stream(handlerList.getRegisteredListeners())
        .map(registeredListener -> registeredListener.getPlugin().getName()).distinct()
        .collect(Collectors.toList());
  }
}