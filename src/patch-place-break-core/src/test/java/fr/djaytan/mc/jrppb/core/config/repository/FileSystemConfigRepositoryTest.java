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
package fr.djaytan.mc.jrppb.core.config.repository;

import static com.google.common.jimfs.Configuration.unix;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Jimfs;
import fr.djaytan.mc.jrppb.core.config.ConfigName;
import java.nio.file.FileSystem;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AutoClose;

final class FileSystemConfigRepositoryTest extends ConfigRepositoryTest {

  @AutoClose private final FileSystem imfs = Jimfs.newFileSystem(unix());
  private final ConfigDirectoryPath configDirectoryPath =
      new ConfigDirectoryPath(imfs.getPath("config"));
  private final ConfigRepository configRepository =
      new FileSystemConfigRepository(configDirectoryPath);

  @Override
  protected @NotNull ConfigRepository configRepository() {
    return configRepository;
  }

  @Override
  protected void assertEmptyDataSource() {
    assertThat(configDirectoryPath.value()).hasToString("/work/config");
    assertThat(configDirectoryPath.value()).doesNotExist();
    assertThat(configDirectoryPath.value().getParent()).isEmptyDirectory();
  }

  @Override
  protected void assertMissingConfigSideEffect(@NotNull ConfigName configName) {
    assertThat(configDirectoryPath.resolveConfigFilePath(configName)).doesNotExist();
  }

  @Override
  protected void assertConfigExistenceSideEffect(@NotNull ConfigName configName) {
    assertThat(configDirectoryPath.resolveConfigFilePath(configName))
        .exists()
        .hasContent(NOMINAL_CONFIG_CONTENT);
  }
}
