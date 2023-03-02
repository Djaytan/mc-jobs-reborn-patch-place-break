package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init;

/**
 * Represents a data source initializer which put at disposition a simple {@link #initialize()}
 * method which must be overridden by the underlying storage implementation (e.g. SQLite or MySQL).
 *
 * <p>The data source must be initialized before establishing any connection (e.g. creating SQLite
 * database before being able to connect onto it).
 */
public interface DataSourceInitializer {

  /** Initializes the data source before establishing any connection. */
  void initialize();
}
