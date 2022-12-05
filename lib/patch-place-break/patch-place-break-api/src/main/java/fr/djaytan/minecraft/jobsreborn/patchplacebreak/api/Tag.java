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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Value;

/**
 * This class represents a place-and-break tag for the patch. The purpose of this tag is to be
 * attached to the metadata of a block.
 *
 * <p>When an instance of this class is found in the metadata of a given block, this means that the
 * patch must be applied for eligible {@link PatchActionType}.
 * See {@link PatchPlaceBreakApi#isPlaceAndBreakAction(PatchActionType, TagLocation)} for
 * more details about it.
 *
 * <p>This tag contains the following information:
 *
 * <ul>
 *   <li>The initial date-time of when the tag as been created ;
 *   <li>The validity duration of the tag. If null, this means that the tag isn't an "ephemeral" one
 *       and will be persisted forever until removed explicitly from metadata of block (e.g. by
 *       calling {@link PatchPlaceBreakApi#removeTag(TagLocation)}).
 * </ul>
 *
 * This class is thread-safe and immutable.
 *
 * @author Djaytan
 * @see PatchActionType
 * @see PatchPlaceBreakApi
 */
@Value(staticConstructor = "of")
public class Tag {

  UUID uuid;
  LocalDateTime initLocalDateTime;
  boolean isEphemeral;
  TagLocation tagLocation;
}
