/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.inject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.inject.Named;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.CharStreams;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.plugin.BukkitException;
import lombok.NonNull;

public class GuicePatchPlaceBreakPluginModule extends AbstractModule {

  private static final String CREATE_TABLE_SQL_SCRIPT_NAME = "database.sql";

  @Provides
  @Named("createTableSqlScriptStream")
  public @NonNull InputStream provideCreateTableSqlScriptStream(@NonNull JavaPlugin javaPlugin) {
    InputStream scriptStream = javaPlugin.getResource(CREATE_TABLE_SQL_SCRIPT_NAME);

    if (scriptStream == null) {
      throw BukkitException.resourceNotFound(CREATE_TABLE_SQL_SCRIPT_NAME);
    }
    return scriptStream;
  }

  @Provides
  @Named("createTableSqlScript")
  public @NonNull String provideCreateTableSqlScript(
      @NonNull @Named("createTableSqlScriptStream") InputStream scriptStream) {
    try {
      return readScriptContent(scriptStream);
    } catch (IOException e) {
      throw BukkitException.malformedResource(CREATE_TABLE_SQL_SCRIPT_NAME);
    }
  }

  private static @NonNull String readScriptContent(@NonNull InputStream scriptStream)
      throws IOException {
    try (Reader reader = new InputStreamReader(scriptStream, StandardCharsets.UTF_8)) {
      return CharStreams.toString(reader);
    }
  }
}
