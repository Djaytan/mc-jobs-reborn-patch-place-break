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
package fr.djaytan.mc.jrppb.core.config;

import static com.google.common.jimfs.Configuration.unix;
import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.NOMINAL_CONFIG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.jimfs.Jimfs;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;

final class ConfigRepositoryTest {

  private static final ConfigName CONFIG_NAME_1 = NOMINAL_CONFIG_NAME;
  private static final ConfigName CONFIG_NAME_2 = new ConfigName("anotherName");

  private static final String NOMINAL_CONFIG_CONTENT = "nominal content";

  @AutoClose private final FileSystem imfs = Jimfs.newFileSystem(unix());
  private final ConfigDirectoryPath configDirectoryPath =
      new ConfigDirectoryPath(imfs.getPath("config"));
  private final ConfigRepository configRepository = new ConfigRepository(configDirectoryPath);

  @Test
  void withoutAnyConfig() {
    assertMissingConfig(CONFIG_NAME_1);
    assertMissingConfig(CONFIG_NAME_2);
    assertThat(configDirectoryPath.value()).hasToString("/work/config");
    assertThat(configDirectoryPath.value()).doesNotExist();
    assertThat(configDirectoryPath.value().getParent()).isEmptyDirectory();
  }

  @Test
  void whenCreatingOneConfig() {
    configRepository.create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT);

    assertConfigExistence(CONFIG_NAME_1);
    assertMissingConfig(CONFIG_NAME_2);
  }

  @Test
  void whenCreatingMultipleConfigs() {
    configRepository.create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT);
    configRepository.create(CONFIG_NAME_2, NOMINAL_CONFIG_CONTENT);

    assertConfigExistence(CONFIG_NAME_1);
    assertConfigExistence(CONFIG_NAME_2);
  }

  @Test
  void whenCreatingAlreadyExistingConfig() {
    configRepository.create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT);

    assertThatThrownBy(() -> configRepository.create(CONFIG_NAME_1, NOMINAL_CONFIG_CONTENT))
        .isExactlyInstanceOf(UncheckedIOException.class)
        .hasMessage("Failed to create config file '/work/config/nominalConfig.conf'")
        .cause()
        .isExactlyInstanceOf(FileAlreadyExistsException.class)
        .hasMessage("/work/config/nominalConfig.conf");
  }

  private void assertMissingConfig(@NotNull ConfigName configName) {
    assertThat(configRepository.exists(configName)).isFalse();
    assertThat(configRepository.findByName(configName)).isEmpty();
    assertThat(configDirectoryPath.resolveConfigFilePath(configName)).doesNotExist();
  }

  private void assertConfigExistence(@NotNull ConfigName configName) {
    assertThat(configRepository.exists(configName)).isTrue();
    assertThat(configRepository.findByName(configName))
        .isPresent()
        .contains(NOMINAL_CONFIG_CONTENT);
    assertThat(configDirectoryPath.resolveConfigFilePath(configName))
        .exists()
        .hasContent(NOMINAL_CONFIG_CONTENT);
  }
}
