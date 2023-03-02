-- NEVER MODIFY THIS FILE
-- Flyway use checksum to detect accidental changes and fail if so

ALTER TABLE ${patchPlaceBreakTableName}
  MODIFY location_x INTEGER;

ALTER TABLE ${patchPlaceBreakTableName}
  MODIFY location_y INTEGER;

ALTER TABLE ${patchPlaceBreakTableName}
  MODIFY location_z INTEGER;
