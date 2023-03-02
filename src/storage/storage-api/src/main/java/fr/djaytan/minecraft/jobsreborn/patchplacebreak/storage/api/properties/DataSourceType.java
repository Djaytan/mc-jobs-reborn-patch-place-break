package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum DataSourceType {
  IN_MEMORY(false),
  MYSQL(true),
  SQLITE(false);

  boolean areCredentialsRequired;
}
