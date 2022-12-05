package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.inject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.inject.Named;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.google.common.io.CharStreams;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.BukkitException;

public class GuiceSpecificBukkitModule extends AbstractModule {

  private static final String CREATE_TABLE_SQL_SCRIPT_NAME = "database.sql";

  private final JavaPlugin javaPlugin;
  private final Logger logger;

  public GuiceSpecificBukkitModule(@NotNull JavaPlugin javaPlugin, @NotNull Logger logger) {
    this.javaPlugin = javaPlugin;
    this.logger = logger;
  }

  @Provides
  @Singleton
  @Named("PatchPlaceBreakLogger")
  public @NotNull Logger provideLogger() {
    return logger;
  }

  @Provides
  @Named("createTableSqlScript")
  public @NotNull String provideCreateTableSqlScript() {
    logger.info("Retrieving SQL table creation script...");
    String scriptName = CREATE_TABLE_SQL_SCRIPT_NAME;
    InputStream scriptStream = getCreateTableSqlScriptStream(scriptName);

    try {
      return readScriptContent(scriptStream);
    } catch (IOException e) {
      throw BukkitException.malformedResource(scriptName);
    }
  }

  private @NotNull InputStream getCreateTableSqlScriptStream(@NotNull String scriptName) {
    InputStream scriptStream = javaPlugin.getResource(scriptName);

    if (scriptStream != null) {
      return scriptStream;
    }
    throw BukkitException.resourceNotFound(scriptName);
  }

  private @NotNull String readScriptContent(InputStream scriptStream) throws IOException {
    try (Reader reader = new InputStreamReader(scriptStream, StandardCharsets.UTF_8)) {
      return CharStreams.toString(reader);
    }
  }
}
