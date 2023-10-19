/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.mc.jrppb.core.storage.sql.provider;

import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceProperties;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class FlywayProvider implements Provider<Flyway> {

  private static final String DB_MIGRATION_DESCRIPTOR_FORMAT = "/db/migration/%s";
  private static final String MIGRATION_HISTORY_TABLE_NAME =
      "patch_place_break_flyway_schema_history";
  private static final String PLACEHOLDER_PATCH_PLACE_BREAK_TABLE_NAME = "patchPlaceBreakTableName";

  private final ClassLoader classLoader;
  private final DataSource dataSource;
  private final DataSourceProperties dataSourceProperties;

  @Inject
  public FlywayProvider(
      @NotNull ClassLoader classLoader,
      @NotNull DataSource dataSource,
      @NotNull DataSourceProperties dataSourceProperties) {
    this.classLoader = classLoader;
    this.dataSource = dataSource;
    this.dataSourceProperties = dataSourceProperties;
  }

  public @NotNull Flyway get() {
    Map<String, String> placeholders = new HashMap<>();
    placeholders.put(PLACEHOLDER_PATCH_PLACE_BREAK_TABLE_NAME, dataSourceProperties.getTable());

    return Flyway.configure(classLoader)
        .baselineOnMigrate(true)
        .dataSource(dataSource)
        .failOnMissingLocations(true)
        .locations(getLocation())
        .loggers("slf4j")
        .placeholders(placeholders)
        .table(MIGRATION_HISTORY_TABLE_NAME)
        .validateOnMigrate(false)
        .validateMigrationNaming(true)
        .load();
  }

  private @NotNull Location getLocation() {
    String descriptor =
        String.format(
            DB_MIGRATION_DESCRIPTOR_FORMAT, dataSourceProperties.getType().name().toLowerCase());
    return new Location(descriptor);
  }
}
