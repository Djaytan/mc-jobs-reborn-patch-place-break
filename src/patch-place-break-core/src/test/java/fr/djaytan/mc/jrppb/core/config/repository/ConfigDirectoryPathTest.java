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
import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.NOMINAL_CONFIG_NAME;
import static fr.djaytan.mc.jrppb.core.config.ConfigNameTestDataSet.randomConfigName;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Jimfs;
import fr.djaytan.mc.jrppb.core.config.ConfigName;
import java.nio.file.FileSystem;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;

final class ConfigDirectoryPathTest {

  private static final String NOMINAL_CONFIG_DIRECTORY_PATH = "/test/test1/test2";

  @AutoClose private final FileSystem imfs = Jimfs.newFileSystem(unix());

  @Test
  void withRootDirectory() {
    assertConfigDirectoryPath("/", "/");
  }

  @Test
  void withMultipleLayersDeepDirectory() {
    assertConfigDirectoryPath("/test/test1/test2", "/test/test1/test2");
  }

  @Test
  void withRelativePath() {
    assertConfigDirectoryPath("", "/work");
  }

  @Test
  void withDenormalizedPath() {
    assertConfigDirectoryPath("/test/test2/../test2", "/test/test2");
  }

  @Test
  void withAnotherConfigName() {
    assertConfigDirectoryPath(
        NOMINAL_CONFIG_DIRECTORY_PATH, NOMINAL_CONFIG_DIRECTORY_PATH, randomConfigName());
  }

  private void assertConfigDirectoryPath(
      @NotNull String inputPath, @NotNull String expectedDirectoryPath) {
    assertConfigDirectoryPath(inputPath, expectedDirectoryPath, NOMINAL_CONFIG_NAME);
  }

  private void assertConfigDirectoryPath(
      @NotNull String inputPath,
      @NotNull String expectedDirectoryPath,
      @NotNull ConfigName expectedConfigName) {
    assertThat(new ConfigDirectoryPath(imfs.getPath(inputPath)))
        .satisfies(
            v ->
                assertThat(v.value())
                    .isAbsolute()
                    .isNormalized()
                    .hasToString(expectedDirectoryPath))
        .satisfies(
            v ->
                assertThat(v.resolveConfigFilePath(expectedConfigName))
                    .hasParentRaw(v.value())
                    .isAbsolute()
                    .isNormalized()
                    .hasFileName(expectedConfigName.value() + ".conf"));
  }
}
