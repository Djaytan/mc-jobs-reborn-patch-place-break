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
package fr.djaytan.mc.jrppb.paper.adapter;

import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializer.serialize;

import fr.djaytan.mc.jrppb.core.config.properties.ConnectionPoolConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.DataSourceConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.DbmsServerConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.DbmsServerCredentialsConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.DbmsServerHostConfigProperties;
import fr.djaytan.mc.jrppb.core.storage.properties.DataSourceType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MariaDBContainer;

class MariadbPatchPlaceBreakPaperAdapterApiIT extends PatchPlaceBreakPaperAdapterApiBaseTest {

  private static final int DATABASE_ORIGINAL_PORT = 3306;
  private static final String DATABASE_NAME = "patch_place_break";

  @SuppressWarnings("resource") // Reusable containers feature enabled: do not clean-up containers!
  private static final MariaDBContainer<?> MARIADB_CONTAINER =
      new MariaDBContainer<>("mariadb:11.1.2-jammy")
          .withDatabaseName(DATABASE_NAME)
          .withReuse(true);

  private static final String CONFIG_DATA_SOURCE_FILE_NAME = "dataSource.conf";

  @BeforeAll
  static void beforeAll() {
    MARIADB_CONTAINER.start();
  }

  @BeforeEach
  void beforeEach() throws IOException {
    int dbmsPort = MARIADB_CONTAINER.getMappedPort(DATABASE_ORIGINAL_PORT);
    String username = MARIADB_CONTAINER.getUsername();
    String password = MARIADB_CONTAINER.getPassword();

    var dataSourceConfigProperties =
        new DataSourceConfigProperties(
            DataSourceType.MYSQL,
            "patch_place_break_tag",
            new DbmsServerConfigProperties(
                new DbmsServerHostConfigProperties("localhost", dbmsPort, true),
                new DbmsServerCredentialsConfigProperties(username, password),
                DATABASE_NAME),
            new ConnectionPoolConfigProperties(30000, 10));

    Path dataSourceConf = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);
    Files.writeString(dataSourceConf, serialize(dataSourceConfigProperties));
    super.beforeEach();
  }
}
