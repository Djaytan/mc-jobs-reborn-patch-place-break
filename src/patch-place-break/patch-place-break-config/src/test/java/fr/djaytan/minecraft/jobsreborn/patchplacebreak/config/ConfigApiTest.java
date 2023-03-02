package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.PropertiesValidator;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigApiTest {

  private ConfigApi configApi;
  private FileSystem imfs;
  private Path dataFolder;

  @BeforeEach
  void beforeEach() {
    imfs = Jimfs.newFileSystem(Configuration.unix());
    dataFolder = imfs.getPath("");
    ConfigSerializer configSerializer = new ConfigSerializer();
    PropertiesValidator propertiesValidator =
        new PropertiesValidator(ValidatorTestWrapper.getValidator());
    ConfigManager configManager =
        new ConfigManager(dataFolder, configSerializer, propertiesValidator);
    configApi = new ConfigApi(configManager);
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    imfs.close();
  }

  @Test
  @DisplayName("When retrieving data source properties")
  @SneakyThrows
  void whenRetrievingDataSourceProperties_shouldSuccess() {
    // Given

    // When
    configApi.getDataSourceProperties();

    // Then
    String expectedContent =
        TestResourcesHelper.getClassResourceAsString(
            this.getClass(), "whenRetrievingDataSourceProperties.conf", false);
    String actualContent = new String(Files.readAllBytes(dataFolder.resolve("dataSource.conf")));
    assertThat(actualContent).containsIgnoringNewLines(expectedContent);
  }
}
