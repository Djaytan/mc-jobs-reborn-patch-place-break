package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.NonNull;
import lombok.Value;

/**
 * This class represents a place-and-break tag for the patch. The purpose of this tag is to be
 * attached to the metadata of a block.
 *
 * <p>When an instance of this class is found in the metadata of a given block, this means that the
 * patch must be applied for eligible {@link BlockActionType}. See {@link
 * PatchPlaceBreakApi#isPlaceAndBreakExploit(BlockActionType, BlockLocation)} for more details about
 * it.
 *
 * <p>This tag contains the following information:
 *
 * <ul>
 *   <li>The initial date-time of when the tag as been created ;
 *   <li>The validity duration of the tag. If null, this means that the tag isn't an "ephemeral" one
 *       and will be persisted forever until removed explicitly from metadata of block (e.g. by
 *       calling {@link PatchPlaceBreakApi#removeTags(BlockLocation)}).
 * </ul>
 *
 * This class is thread-safe and immutable.
 */
@Value(staticConstructor = "of")
public class Tag {

  @NonNull UUID uuid;
  @NonNull LocalDateTime initLocalDateTime;
  boolean isEphemeral;
  @NonNull BlockLocation blockLocation;
}
