package fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ClassToFsPathConverterTest {

  private ClassToFsPathConverter classToFsPathConverter;
  private FileSystem fileSystem;

  @BeforeEach
  void beforeEach() {
    this.fileSystem = Jimfs.newFileSystem(Configuration.unix());
    this.classToFsPathConverter = new ClassToFsPathConverter(fileSystem);
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    fileSystem.close();
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("With top-level class")
    void withTopLevelClass_shouldMatchExpectedPath() {
      // Given
      Class<?> clazz = ClassToFsPathConverterTest.class;

      // When
      Path path = classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      assertThat(path)
          .hasToString(
              "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/ClassToFsPathConverterTest");
    }

    @Test
    @DisplayName("With inner class")
    void withInnerClass_shouldMatchExpectedPath() {
      // Given
      Class<?> clazz = WhenConverting.class;

      // When
      Path path = classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      assertThat(path)
          .hasToString(
              "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/ClassToFsPathConverterTest");
    }

    @Test
    @DisplayName("With anonymous class")
    void withAnonymousClass_shouldMatchExpectedPath() {
      // Given
      Class<?> clazz = new Serializable() {}.getClass();

      // When
      Path path = classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      assertThat(path)
          .hasToString(
              "fr/djaytan/minecraft/jobsreborn/patchplacebreak/commons/test/ClassToFsPathConverterTest");
    }

    @Test
    @DisplayName("With primitive class")
    void withPrimitiveClass_shouldThrowException() {
      // Given
      Class<?> clazz = int.class;

      // When
      ThrowingCallable throwingCallable = () -> classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      Assertions.assertThatThrownBy(throwingCallable)
          .isExactlyInstanceOf(UnsupportedClassException.class);
    }

    @Test
    @DisplayName("With array class")
    void withArrayClass_shouldThrowException() {
      // Given
      Class<?> clazz = Object[].class;

      // When
      ThrowingCallable throwingCallable = () -> classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      Assertions.assertThatThrownBy(throwingCallable)
          .isExactlyInstanceOf(UnsupportedClassException.class);
    }

    @Test
    @DisplayName("With annotation class")
    void withAnnotationClass_shouldThrowException() {
      // Given
      Class<?> clazz = Retention.class;

      // When
      ThrowingCallable throwingCallable = () -> classToFsPathConverter.convertClassToFsPath(clazz);

      // Then
      Assertions.assertThatThrownBy(throwingCallable)
          .isExactlyInstanceOf(UnsupportedClassException.class);
    }
  }
}
