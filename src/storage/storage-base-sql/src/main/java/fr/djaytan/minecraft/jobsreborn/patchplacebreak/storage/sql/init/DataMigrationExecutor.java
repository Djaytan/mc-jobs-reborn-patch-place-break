package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.init;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.NonNull;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;

/**
 * Represents the data migration executor.
 *
 * <p>It is in charge of performing data migrations with {@link #migrate(DataSource)} method.
 *
 * <p>Interactions with this class are expected only after having established connections with the
 * data source (i.e. after having enabled the connection pool).
 */
@Singleton
public class DataMigrationExecutor {

  private static final String MIGRATION_HISTORY_TABLE_NAME =
      "patch_place_break_flyway_schema_history";
  private static final String PLACEHOLDER_PATCH_PLACE_BREAK_TABLE_NAME = "patchPlaceBreakTableName";

  private final ClassLoader classLoader;
  private final DataSourceProperties dataSourceProperties;
  private final Location location;

  @Inject
  public DataMigrationExecutor(
      ClassLoader classLoader, DataSourceProperties dataSourceProperties, Location location) {
    this.classLoader = classLoader;
    this.dataSourceProperties = dataSourceProperties;
    this.location = location;
  }

  /**
   * Performs migrations into the currently in-used data source. Once done, validates that
   * migrations have been well-performed and are up-to-date.
   *
   * @param dataSource The data source to be used when establishing connections at migration time.
   */
  public void migrate(@NonNull DataSource dataSource) {
    Flyway flyway = prepare(dataSource);
    flyway.migrate();
    flyway.validate();
  }

  private @NonNull Flyway prepare(@NonNull DataSource dataSource) {
    Map<String, String> placeholders = new HashMap<>();
    placeholders.put(PLACEHOLDER_PATCH_PLACE_BREAK_TABLE_NAME, dataSourceProperties.getTable());

    return Flyway.configure(classLoader)
        .baselineOnMigrate(true)
        .dataSource(dataSource)
        .failOnMissingLocations(true)
        .locations(location)
        .loggers("slf4j")
        .placeholders(placeholders)
        .table(MIGRATION_HISTORY_TABLE_NAME)
        .validateOnMigrate(false)
        .validateMigrationNaming(true)
        .load();
  }
}
