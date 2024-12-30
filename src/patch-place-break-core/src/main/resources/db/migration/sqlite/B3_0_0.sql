-- NEVER MODIFY THIS FILE
-- Flyway use checksum to detect accidental changes and fail if so

CREATE TABLE ${patchPlaceBreakTableName}
(
  world_name           TEXT    NOT NULL,
  location_x           INTEGER NOT NULL,
  location_y           INTEGER NOT NULL,
  location_z           INTEGER NOT NULL,
  is_ephemeral         INTEGER NOT NULL,
  created_at_timestamp TEXT    NOT NULL,
  PRIMARY KEY (world_name, location_x, location_y, location_z)
);
