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
package fr.djaytan.mc.jrppb.commons.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ClassToFsPathConverterTest {

  @AutoClose private final FileSystem imfs = Jimfs.newFileSystem(Configuration.unix());

  private ClassToFsPathConverter classToFsPathConverter;

  @BeforeEach
  void setUp() {
    this.classToFsPathConverter = new ClassToFsPathConverter(imfs);
  }

  @Nested
  class WhenConverting {

    @Test
    void withTopLevelClass_shouldMatchExpectedPath() {
      // Given
      Class<?> clazz = ClassToFsPathConverterTest.class;

      // When
      Path path = classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      assertThat(path).hasToString("fr/djaytan/mc/jrppb/commons/test/ClassToFsPathConverterTest");
    }

    @Test
    void withInnerClass_shouldMatchExpectedPath() {
      // Given
      Class<?> clazz = WhenConverting.class;

      // When
      Path path = classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      assertThat(path).hasToString("fr/djaytan/mc/jrppb/commons/test/ClassToFsPathConverterTest");
    }

    @Test
    void withAnonymousClass_shouldMatchExpectedPath() {
      // Given
      Class<?> clazz = new Serializable() {}.getClass();

      // When
      Path path = classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      assertThat(path).hasToString("fr/djaytan/mc/jrppb/commons/test/ClassToFsPathConverterTest");
    }

    @Test
    void withPrimitiveClass_shouldThrowException() {
      // Given
      Class<?> clazz = int.class;

      // When
      Exception exception =
          catchException(() -> classToFsPathConverter.convertClassToFsPath(clazz));

      // Then
      assertThat(exception)
          .isExactlyInstanceOf(UnsupportedOperationException.class)
          .hasMessage("The class 'int' isn't supported for resource conversion.");
    }

    @Test
    void withArrayClass_shouldThrowException() {
      // Given
      Class<?> clazz = Object[][].class;

      // When
      Exception exception =
          catchException(() -> classToFsPathConverter.convertClassToFsPath(clazz));

      // Then
      assertThat(exception)
          .isExactlyInstanceOf(UnsupportedOperationException.class)
          .hasMessage("The class 'java.lang.Object[][]' isn't supported for resource conversion.");
    }

    @Test
    void withAnnotationClass_shouldThrowException() {
      // Given
      Class<?> clazz = Retention.class;

      // When
      Exception exception =
          catchException(() -> classToFsPathConverter.convertClassToFsPath(clazz));

      // Then
      assertThat(exception)
          .isExactlyInstanceOf(UnsupportedOperationException.class)
          .hasMessage(
              "The class 'java.lang.annotation.Retention' isn't supported for resource conversion.");
    }
  }
}
