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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.PatchPlaceBreakException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchPlaceBreakApi;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import java.nio.file.Path;
import lombok.NonNull;

public final class PatchPlaceBreakInjector {

  private Injector injector;

  public void inject(@NonNull ClassLoader classLoader, @NonNull Path dataFolder) {
    injector =
        Guice.createInjector(
            new ConfigModule(),
            new StorageModule(),
            new ValidationModule(),
            new PatchPlaceBreakModule(classLoader, dataFolder));
  }

  public @NonNull PatchPlaceBreakApi getPatchApi() {
    if (injector == null) {
      throw PatchPlaceBreakException.injectionRequiredFirst();
    }
    return injector.getInstance(PatchPlaceBreakApi.class);
  }

  public @NonNull DataSourceManager getDataSourceManager() {
    if (injector == null) {
      throw PatchPlaceBreakException.injectionRequiredFirst();
    }
    return injector.getInstance(DataSourceManager.class);
  }
}
