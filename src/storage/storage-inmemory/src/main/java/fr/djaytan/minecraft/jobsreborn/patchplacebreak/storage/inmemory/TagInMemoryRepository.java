package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.inmemory;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.TagRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public class TagInMemoryRepository implements TagRepository {

  private final Map<UUID, Tag> tagMap = new HashMap<>();

  @Override
  public void put(@NonNull Tag tag) {
    tagMap.put(tag.getUuid(), tag);
  }

  @Override
  public @NonNull Optional<Tag> findByLocation(@NonNull BlockLocation blockLocation) {
    return tagMap.values().stream()
        .filter(tag -> tag.getBlockLocation().equals(blockLocation))
        .findFirst();
  }

  @Override
  public void delete(@NonNull BlockLocation blockLocation) {
    Optional<Tag> tag = findByLocation(blockLocation);
    tag.ifPresent(t -> tagMap.remove(t.getUuid()));
  }
}
