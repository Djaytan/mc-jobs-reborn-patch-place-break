package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql;

import lombok.NonNull;

/** Represents a JDBC URL required to establish connexion with underlying data source. */
public interface JdbcUrl {

  /**
   * Returns the JDBC URL of the underlying data source.
   *
   * @return The JDBC URL of the underlying data source.
   */
  @NonNull
  String get();
}
