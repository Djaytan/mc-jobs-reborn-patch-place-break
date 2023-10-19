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
package fr.djaytan.mc.jrppb.bukkit.adapter;

import fr.djaytan.mc.jrppb.commons.test.TestResourcesHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;

class MariadbPatchPlaceBreakBukkitAdapterApiIntegrationTest
    extends BasePatchPlaceBreakBukkitAdapterApiIntegrationTest {

  private static final String CONFIG_DATA_SOURCE_FILE_NAME = "dataSource.conf";

  @BeforeEach
  void beforeEach() throws IOException {
    int dbmsPort = Integer.parseInt(System.getProperty("mariadb.port"));
    String givenDataSourceConfFileContent =
        String.format(
            TestResourcesHelper.getClassResourceAsString(this.getClass(), "mariadb.conf", false),
            dbmsPort);
    Path dataSourceConf = dataFolder.resolve(CONFIG_DATA_SOURCE_FILE_NAME);
    Files.writeString(dataSourceConf, givenDataSourceConfFileContent);
    super.beforeEach();
  }
}
