package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/** Default implementation of {@link PatchPlaceBreakApi}. */
@Singleton
@Slf4j
public class PatchPlaceBreakImpl implements PatchPlaceBreakApi {

  private final TagRepository tagRepository;

  @Inject
  public PatchPlaceBreakImpl(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public void putTag(@NonNull BlockLocation blockLocation, boolean isEphemeral) {
    CompletableFuture.runAsync(
        () -> {
          UUID tagUuid = UUID.randomUUID();
          LocalDateTime localDateTime = LocalDateTime.now();
          Tag tag = Tag.of(tagUuid, localDateTime, isEphemeral, blockLocation);
          tagRepository.put(tag);
        });
  }

  public void moveTags(
      @NonNull Collection<BlockLocation> blockLocations, @NonNull Vector direction) {
    CompletableFuture.runAsync(
        () -> {
          for (BlockLocation oldBlockLocation : blockLocations) {
            Optional<Tag> tag = tagRepository.findByLocation(oldBlockLocation);

            if (!tag.isPresent()) {
              continue;
            }

            BlockLocation newBlockLocation = BlockLocation.from(oldBlockLocation, direction);

            moveTag(oldBlockLocation, newBlockLocation, tag.get().isEphemeral());
          }
        });
  }

  private void moveTag(
      @NonNull BlockLocation oldBlockLocation,
      @NonNull BlockLocation newBlockLocation,
      boolean isEphemeral) {
    putTag(newBlockLocation, isEphemeral);
    // TODO: old tag must be removed, but it must be done with a transaction
  }

  public void removeTags(@NonNull BlockLocation blockLocation) {
    CompletableFuture.runAsync(() -> tagRepository.delete(blockLocation));
  }

  public boolean isPlaceAndBreakExploit(
      @NonNull BlockActionType blockActionType, @NonNull BlockLocation blockLocation) {
    Optional<Tag> tag = tagRepository.findByLocation(blockLocation);

    if (!tag.isPresent()) {
      return false;
    }

    if (!tag.get().isEphemeral()) {
      return true;
    }

    LocalDateTime localDateTime = LocalDateTime.now();
    Duration timeElapsed = Duration.between(tag.get().getInitLocalDateTime(), localDateTime);

    return timeElapsed.minus(EPHEMERAL_TAG_DURATION).isNegative();
  }
}
