package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class PatchPlaceBreakStandaloneException extends RuntimeException {

  private static final String UNRECOGNISED_DATA_SOURCE_TYPE = "Unrecognised data source type: %s";
  private static final String UNSUPPORTED_DATA_SOURCE_TYPE = "Unsupported data source type: %s";

  public static @NonNull PatchPlaceBreakStandaloneException unrecognisedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNRECOGNISED_DATA_SOURCE_TYPE, dataSourceType.name());
    return new PatchPlaceBreakStandaloneException(message);
  }

  public static @NonNull PatchPlaceBreakStandaloneException unsupportedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNSUPPORTED_DATA_SOURCE_TYPE, dataSourceType.name());
    UnsupportedOperationException e = new UnsupportedOperationException(message);
    return new PatchPlaceBreakStandaloneException(e);
  }
}
