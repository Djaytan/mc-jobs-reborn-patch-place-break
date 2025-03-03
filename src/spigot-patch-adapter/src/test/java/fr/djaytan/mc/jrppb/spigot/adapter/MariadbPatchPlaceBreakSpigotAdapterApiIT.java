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
package fr.djaytan.mc.jrppb.spigot.adapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MariaDBContainer;

class MariadbPatchPlaceBreakSpigotAdapterApiIT extends PatchPlaceBreakSpigotAdapterApiBaseTest {

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
    String givenDataSourceConfFileContent =
        """
        #         JobsReborn-PatchPlaceBreak
        # A patch place-break extension for JobsReborn
        #                (by Djaytan)
        #\s
        # This config file use HOCON format
        # Specifications are here: https://github.com/lightbend/config/blob/main/HOCON.md
        #\s
        # /!\\ Properties ordering is nondeterministic at config generation time because of limitations
        # of underlying library.

        # Connection pool properties
        # This is reserved for advanced usage only
        # Change these settings only if you know what you are doing
        connectionPool {
            # The connection timeout (in milliseconds)
            # Corresponds to the maximum time the connection pool will wait to acquire a new connection
            # from the DBMS server
            # Not applicable for SQLite
            # Accepted range values: [1-600000]
            connectionTimeout=30000
            # The number of DBMS connections in the pool
            # Could be best determined by the executing environment
            # Accepted range values: [1-100]
            poolSize=10
        }
        # The DBMS server properties for connection establishment
        # Not applicable for SQLite
        dbmsServer {
            # Credentials for authentication with the DBMS server
            credentials {
                # Password of the user (optional but highly recommended)
                password="%3$s"
                # Under behalf of which user to connect on the DBMS server
                # Value can't be empty or blank
                username="%2$s"
            }
            # The database to use on DBMS server
            # Value can't be empty or blank
            database="patch_place_break"
            # Host properties of the DBMS server
            host {
                # Hostname (an IP address (IPv4/IPv6) or a domain name)
                # Value can't be empty or blank
                hostname=localhost
                # Whether an SSL/TLS communication must be established at connection time (more secure)
                # Only boolean values accepted (true|false)
                isSslEnabled=true
                # Port
                # Accepted range values: [1-65535]
                port=%1$d
            }
        }
        # The table where data will be stored
        # Value can't be empty or blank
        table="patch_place_break_tag"
        # The type of datasource to use
        # Available types:
        # * SQLITE: use a local file as database (easy & fast setup)
        # * MYSQL: use a MySQL database server (better performances)
        type=MYSQL
        """
            .formatted(dbmsPort, username, password);
    Path dataSourceConf = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);
    Files.writeString(dataSourceConf, givenDataSourceConfFileContent);
    super.beforeEach();
  }
}
