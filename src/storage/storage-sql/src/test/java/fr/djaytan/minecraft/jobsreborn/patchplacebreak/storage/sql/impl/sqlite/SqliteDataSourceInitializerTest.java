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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.impl.sqlite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.SqlStorageException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class SqliteDataSourceInitializerTest {

  private static final String DUMMY_FILE_NAME = "dummy.db";

  private final FileSystem imfs = Jimfs.newFileSystem(Configuration.unix());
  private final Path dummyFile = imfs.getPath(DUMMY_FILE_NAME);
  private final SqliteDataSourceInitializer sqliteDataSourceInitializer =
      new SqliteDataSourceInitializer(dummyFile);

  @AfterEach
  @SneakyThrows
  void afterEach() {
    imfs.close();
  }

  @Test
  @DisplayName("When initializing with existing database file")
  @SneakyThrows
  void whenInitializing_withExistingDatabaseFile() {
    // Given
    String dummyContent = "A dummy content";
    Files.createFile(dummyFile);
    Files.write(dummyFile, dummyContent.getBytes(StandardCharsets.UTF_8));

    // When
    sqliteDataSourceInitializer.initialize();

    // Then
    assertThat(dummyFile).exists().hasContent(dummyContent);
  }

  @Test
  @DisplayName("When initializing without existing database file")
  void whenInitializing_withoutExistingDatabaseFile() {
    // Given

    // When
    sqliteDataSourceInitializer.initialize();

    // Then
    assertThat(dummyFile).exists().isEmptyFile();
  }

  @Test
  @DisplayName("When initializing while IOException occurs")
  @SneakyThrows
  void whenInitializing_whileIOExceptionOccurs() {
    // Given
    try (MockedStatic<Files> mockedStatic = mockStatic(Files.class)) {
      mockedStatic.when(() -> Files.createFile(any())).thenThrow(IOException.class);

      // When
      ThrowingCallable throwingCallable = sqliteDataSourceInitializer::initialize;

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(SqlStorageException.class);
    }
  }
}
