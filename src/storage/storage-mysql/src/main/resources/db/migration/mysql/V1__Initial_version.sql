CREATE TABLE ${patchPlaceBreakTableName}
(
  tag_uuid       CHAR(36) PRIMARY KEY NOT NULL,
  init_timestamp VARCHAR(64)          NOT NULL,
  is_ephemeral   INTEGER              NOT NULL,
  world_name     VARCHAR(128)         NOT NULL,
  location_x     DOUBLE               NOT NULL,
  location_y     DOUBLE               NOT NULL,
  location_z     DOUBLE               NOT NULL
);
