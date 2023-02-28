/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
