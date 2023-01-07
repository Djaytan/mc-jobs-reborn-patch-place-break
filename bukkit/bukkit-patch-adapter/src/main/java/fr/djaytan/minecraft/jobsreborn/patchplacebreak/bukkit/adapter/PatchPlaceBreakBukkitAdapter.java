/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.gamingmesh.jobs.container.ActionType;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.PatchActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagVector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.ActionTypeConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.BlockFaceConverter;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter.LocationConverter;
import lombok.NonNull;

@Singleton
public class PatchPlaceBreakBukkitAdapter {

  private final ActionTypeConverter actionTypeConverter;
  private final LocationConverter locationConverter;
  private final PatchPlaceBreakApi patchPlaceBreakApi;
  private final BlockFaceConverter blockFaceConverter;

  @Inject
  PatchPlaceBreakBukkitAdapter(ActionTypeConverter actionTypeConverter,
      LocationConverter locationConverter, PatchPlaceBreakApi patchPlaceBreakApi,
      BlockFaceConverter blockFaceConverter) {
    this.actionTypeConverter = actionTypeConverter;
    this.locationConverter = locationConverter;
    this.patchPlaceBreakApi = patchPlaceBreakApi;
    this.blockFaceConverter = blockFaceConverter;
  }

  @CanIgnoreReturnValue
  public @NonNull CompletableFuture<Void> putTag(@NonNull Location location, boolean isEphemeral) {
    TagLocation tagLocation = locationConverter.convert(location);
    return patchPlaceBreakApi.putTag(tagLocation, isEphemeral);
  }

  @CanIgnoreReturnValue
  public @NonNull CompletableFuture<Void> putBackTagOnMovedBlocks(@NonNull Collection<Block> blocks,
      @NonNull BlockFace blockFace) {
    Collection<TagLocation> tagLocations = blocks.stream().map(Block::getLocation)
        .map(locationConverter::convert).collect(Collectors.toList());
    TagVector tagVector = blockFaceConverter.convert(blockFace);
    return patchPlaceBreakApi.putBackTagOnMovedBlocks(tagLocations, tagVector);
  }

  @CanIgnoreReturnValue
  public @NonNull CompletableFuture<Void> removeTag(@NonNull Location location) {
    TagLocation tagLocation = locationConverter.convert(location);
    return patchPlaceBreakApi.removeTag(tagLocation);
  }

  public @NonNull CompletableFuture<Boolean> isPlaceAndBreakAction(@NonNull ActionType actionType,
      @NonNull Location location) {
    PatchActionType patchActionType = actionTypeConverter.convert(actionType);
    TagLocation tagLocation = locationConverter.convert(location);
    return patchPlaceBreakApi.isPlaceAndBreakAction(patchActionType, tagLocation);
  }
}
