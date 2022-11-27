/*
 * JobsReborn extension to patch place-break (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import com.gamingmesh.jobs.container.ActionType;
import com.google.common.base.MoreObjects;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller.PatchPlaceAndBreakJobsController;

/**
 * This class represents a place-and-break tag for the patch. The purpose of this tag is to be
 * attached to the metadata of a {@link Block}.
 *
 * <p>When an instance of this class is found in the metadata of a given block, this means that the
 * patch must be applied for eligible {@link ActionType}. See {@link
 * PatchPlaceAndBreakJobsController#isPlaceAndBreakAction(ActionType, Location)} for more details about
 * it.
 *
 * <p>This tag contains the following information:
 *
 * <ul>
 *   <li>The initial date-time of when the tag as been created ;
 *   <li>The validity duration of the tag. If null, this means that the tag isn't an "ephemeral" one
 *       and will be persisted forever until removed explicitly from metadata of block (e.g. by
 *       calling {@link PatchPlaceAndBreakJobsController#removeTag(Location)}).
 * </ul>
 *
 * This class is thread-safe and immutable.
 *
 * @author Djaytan
 * @see com.gamingmesh.jobs.container.ActionType ActionType
 * @see org.bukkit.block.Block Block
 * @see PatchPlaceAndBreakJobsController
 */
public final class PatchPlaceAndBreakTag {

  private final UUID uuid;
  private final LocalDateTime initLocalDateTime;
  private final boolean isEphemeral;
  private final TagLocation tagLocation;

  private PatchPlaceAndBreakTag(@NotNull UUID uuid, @NotNull LocalDateTime initLocalDateTime,
      boolean isEphemeral, @NotNull TagLocation tagLocation) {
    this.uuid = uuid;
    this.initLocalDateTime = initLocalDateTime;
    this.isEphemeral = isEphemeral;
    this.tagLocation = tagLocation;
  }

  public static @NotNull PatchPlaceAndBreakTag of(@NotNull UUID uuid,
      @NotNull LocalDateTime initLocalDateTime, boolean isEphemeral,
      @NotNull TagLocation tagLocation) {
    return new PatchPlaceAndBreakTag(uuid, initLocalDateTime, isEphemeral, tagLocation);
  }

  public @NotNull UUID getUuid() {
    return uuid;
  }

  public @NotNull LocalDateTime getInitLocalDateTime() {
    return initLocalDateTime;
  }

  public boolean isEphemeral() {
    return isEphemeral;
  }

  public @NotNull TagLocation getTagLocation() {
    return tagLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PatchPlaceAndBreakTag that = (PatchPlaceAndBreakTag) o;
    return isEphemeral == that.isEphemeral && uuid.equals(that.uuid)
        && initLocalDateTime.equals(that.initLocalDateTime) && tagLocation.equals(that.tagLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, initLocalDateTime, isEphemeral, tagLocation);
  }

  @Override
  public @NotNull String toString() {
    return MoreObjects.toStringHelper(this).add("uuid", uuid)
        .add("initLocalDateTime", initLocalDateTime).add("isEphemeral", isEphemeral)
        .add("tagLocation", tagLocation).toString();
  }
}
