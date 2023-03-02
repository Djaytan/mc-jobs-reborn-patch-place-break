package fr.djaytan.minecraft.jobsreborn.patchplacebreak;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class PatchPlaceBreakException extends RuntimeException {

  private static final String INJECTION_REQUIRED_FIRST =
      "The dependencies injection must be achieved first.";
  private static final String UNRECOGNISED_DATA_SOURCE_TYPE = "Unrecognised data source type: %s";
  private static final String UNSUPPORTED_DATA_SOURCE_TYPE = "Unsupported data source type: %s";

  public static @NonNull PatchPlaceBreakException injectionRequiredFirst() {
    return new PatchPlaceBreakException(INJECTION_REQUIRED_FIRST);
  }

  public static @NonNull PatchPlaceBreakException unrecognisedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNRECOGNISED_DATA_SOURCE_TYPE, dataSourceType.name());
    return new PatchPlaceBreakException(message);
  }

  public static @NonNull PatchPlaceBreakException unsupportedDataSourceType(
      @NonNull DataSourceType dataSourceType) {
    String message = String.format(UNSUPPORTED_DATA_SOURCE_TYPE, dataSourceType.name());
    UnsupportedOperationException e = new UnsupportedOperationException(message);
    return new PatchPlaceBreakException(e);
  }
}
