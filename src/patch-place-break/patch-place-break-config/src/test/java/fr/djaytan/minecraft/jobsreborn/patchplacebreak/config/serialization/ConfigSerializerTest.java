package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DbmsHostValidatingProperties;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import lombok.SneakyThrows;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.serialize.SerializationException;

class ConfigSerializerTest {

  private ConfigSerializer configSerializer = new ConfigSerializer();
  private FileSystem imfs;

  @BeforeEach
  void beforeEach() {
    configSerializer = new ConfigSerializer();
    imfs = Jimfs.newFileSystem(Configuration.unix());
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    imfs.close();
  }

  @Nested
  @DisplayName("When serializing")
  class WhenSerializing {

    @Test
    @DisplayName("With nominal values")
    @SneakyThrows
    void withNominalValues_shouldCreateAndFillYamlFile() {
      // Given
      Path targetFileLocation = imfs.getPath("test.conf");
      DbmsHostValidatingProperties dbmsHostValidatingProperties =
          DbmsHostValidatingProperties.of("example.com", 1234, true);

      // When
      configSerializer.serialize(targetFileLocation, dbmsHostValidatingProperties);

      // Then
      String expectedYamlFile = "whenSerializing_withNominalValues.conf";
      String expectedYaml =
          TestResourcesHelper.getClassResourceAsString(this.getClass(), expectedYamlFile, false);
      String actualYaml = new String(Files.readAllBytes(targetFileLocation));
      assertThat(actualYaml).isEqualToIgnoringNewLines(expectedYaml);
    }
  }

  @Nested
  @DisplayName("When deserializing")
  class WhenDeserializing {

    @Test
    @DisplayName("With camelCase fields")
    void withCamelCaseFields_shouldSuccess() {
      // Given
      String confFileName = "whenDeserializing_withCamelCaseFields.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsHostValidatingProperties> optionalHostValidatingProperties =
          configSerializer.deserialize(confFile, DbmsHostValidatingProperties.class);

      // Then
      assertThat(optionalHostValidatingProperties)
          .isPresent()
          .get()
          .isEqualTo(DbmsHostValidatingProperties.of("example.com", 1234, true));
    }

    @Test
    @DisplayName("With kebab-case fields")
    void withKebabCaseFields_shouldThrowException() {
      // Given
      String confFileName = "whenDeserializing_withKebabCaseFields.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      ThrowingCallable throwingCallable =
          () -> configSerializer.deserialize(confFile, DbmsHostValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable)
          .isExactlyInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }
  }
}
