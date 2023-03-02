-- NEVER MODIFY THIS FILE
-- Flyway use checksum to detect accidental changes and fail if so

CREATE TABLE ${patchPlaceBreakTableName}_migration
(
  tag_uuid       TEXT PRIMARY KEY NOT NULL,
  init_timestamp TEXT             NOT NULL,
  is_ephemeral   INTEGER          NOT NULL,
  world_name     TEXT             NOT NULL,
  location_x     INTEGER          NOT NULL,
  location_y     INTEGER          NOT NULL,
  location_z     INTEGER          NOT NULL
);

INSERT INTO ${patchPlaceBreakTableName}_migration
SELECT *
FROM ${patchPlaceBreakTableName};

DROP TABLE ${patchPlaceBreakTableName};

ALTER TABLE ${patchPlaceBreakTableName}_migration
  RENAME TO ${patchPlaceBreakTableName};
