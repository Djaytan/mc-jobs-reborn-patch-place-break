package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class PatchPlaceBreakCoreException extends RuntimeException {

  private static final String INJECTION_REQUIRED_FIRST =
      "The dependencies injection must be achieved first.";
  private static final String UNRECOGNISED_DATA_SOURCE_TYPE = "Unrecognised data source type: %s";
  private static final String UNSUPPORTED_DATA_SOURCE_TYPE = "Unsupported data source type: %s";

  public static @NonNull PatchPlaceBreakCoreException injectionRequiredFirst() {
    return new PatchPlaceBreakCoreException(INJECTION_REQUIRED_FIRST);
  }

  public static @NonNull PatchPlaceBreakCoreException unrecognisedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNRECOGNISED_DATA_SOURCE_TYPE, dataSourceType.name());
    return new PatchPlaceBreakCoreException(message);
  }

  public static @NonNull PatchPlaceBreakCoreException unsupportedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNSUPPORTED_DATA_SOURCE_TYPE, dataSourceType.name());
    UnsupportedOperationException e = new UnsupportedOperationException(message);
    return new PatchPlaceBreakCoreException(e);
  }
}
