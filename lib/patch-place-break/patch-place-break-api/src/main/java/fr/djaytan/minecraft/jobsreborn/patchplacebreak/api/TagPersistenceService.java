package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

public interface TagPersistenceService {

  @NotNull
  CompletableFuture<Void> persistTag(boolean isEphemeral, @NotNull TagLocation tagLocation);

  CompletableFuture<Optional<Tag>> findTagByLocation(@NotNull TagLocation tagLocation);

  @NotNull
  CompletableFuture<Void> removeTag(@NotNull TagLocation tagLocation);
}
