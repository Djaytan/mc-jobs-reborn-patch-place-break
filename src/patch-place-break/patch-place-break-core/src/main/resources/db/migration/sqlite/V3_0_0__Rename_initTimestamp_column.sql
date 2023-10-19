-- NEVER MODIFY THIS FILE
-- Flyway use checksum to detect accidental changes and fail if so

ALTER TABLE ${patchPlaceBreakTableName}
  RENAME COLUMN init_timestamp TO created_at_timestamp;
