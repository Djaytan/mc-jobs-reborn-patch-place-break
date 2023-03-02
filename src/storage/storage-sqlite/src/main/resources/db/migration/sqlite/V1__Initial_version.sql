-- NEVER MODIFY THIS FILE
-- Flyway use checksum to detect accidental changes and fail if so

CREATE TABLE ${patchPlaceBreakTableName}
(
  tag_uuid       TEXT PRIMARY KEY NOT NULL,
  init_timestamp TEXT             NOT NULL,
  is_ephemeral   INTEGER          NOT NULL,
  world_name     TEXT             NOT NULL,
  location_x     REAL             NOT NULL,
  location_y     REAL             NOT NULL,
  location_z     REAL             NOT NULL
);
