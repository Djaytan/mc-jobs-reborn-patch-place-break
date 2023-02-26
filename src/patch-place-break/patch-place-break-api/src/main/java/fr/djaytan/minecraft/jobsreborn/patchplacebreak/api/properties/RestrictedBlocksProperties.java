package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RestrictedBlocksProperties {

  @NonNull Set<String> materials;
  @NonNull RestrictionMode mode;

  public static @NonNull RestrictedBlocksProperties of(
    @NonNull Set<String> materials, @NonNull RestrictionMode mode) {
    return new RestrictedBlocksProperties(ImmutableSet.copyOf(materials), mode);
  }
}
