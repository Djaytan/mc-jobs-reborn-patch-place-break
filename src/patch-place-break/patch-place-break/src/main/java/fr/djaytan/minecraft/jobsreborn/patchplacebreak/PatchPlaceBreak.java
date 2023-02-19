/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject.PatchPlaceBreakInjector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSource;
import java.nio.file.Path;
import javax.inject.Singleton;
import lombok.NonNull;

/**
 * This class represents the patch place-break enabler for any consumer. Its role is to manage the
 * patch's lifecycle.
 *
 * <p>When enabling the patch with {@link #enable(Path)}, it is ensured to be properly initialized
 * and a {@link PatchPlaceBreakApi} instance is returned to be consumed by the caller.
 *
 * <p>When no longer using the patch, ensure to call the {@link #disable()} method (e.g. on plugin
 * disabling). This will ensure to properly disable the patch (e.g. by closing existing databases
 * connections).
 *
 * <p>Only one instance of this class is expected to be run at once.
 */
@Singleton
public class PatchPlaceBreak {

  private DataSource dataSource;

  public @NonNull PatchPlaceBreakApi enable(@NonNull Path dataFolder) {
    setupSlf4j();
    PatchPlaceBreakInjector injector = new PatchPlaceBreakInjector();
    injector.inject(dataFolder);
    dataSource = injector.getDataSource();
    dataSource.connect();
    return injector.getPatchApi();
  }

  public void disable() {
    dataSource.disconnect();
  }

  /**
   * jboss-logging shall use SLF4J as logger provider.
   *
   * <p>This is required since jboss-logging is a dependency of hibernate-validator.
   */
  private void setupSlf4j() {
    System.setProperty("org.jboss.logging.provider", "slf4j");
  }
}
