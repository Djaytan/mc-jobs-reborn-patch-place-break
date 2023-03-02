package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class TagRepositoryException extends RuntimeException {

  private static final String PUT = "Failed to put the following tag: %s";
  private static final String FETCH = "Failed to fetch the tag with the following location: %s";
  private static final String DELETE = "Failed to delete the tag with the following location: %s";

  public static @NonNull TagRepositoryException put(@NonNull Tag tag, @NonNull Throwable cause) {
    String message = String.format(PUT, tag);
    return new TagRepositoryException(message, cause);
  }

  public static @NonNull TagRepositoryException fetch(
      @NonNull BlockLocation blockLocation, @NonNull Throwable cause) {
    String message = String.format(FETCH, blockLocation);
    return new TagRepositoryException(message, cause);
  }

  public static @NonNull TagRepositoryException delete(
      @NonNull BlockLocation blockLocation, @NonNull Throwable cause) {
    String message = String.format(DELETE, blockLocation);
    return new TagRepositoryException(message, cause);
  }
}
