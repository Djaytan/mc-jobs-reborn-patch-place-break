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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.mysql;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.api.StorageException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.sql.SqlDataSourceInitializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.impl.storage.sql.SqlHelper;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

@Singleton
public class MysqlDataSourceInitializer extends SqlDataSourceInitializer {

  private final DataSourceProperties dataSourceProperties;

  @Inject
  public MysqlDataSourceInitializer(
      @NonNull DataSourceProperties dataSourceProperties, @NonNull SqlHelper sqlHelper) {
    super(sqlHelper);
    this.dataSourceProperties = dataSourceProperties;
  }

  @Override
  public void initialize() throws StorageException {
    Validate.validState(
        dataSourceProperties.getType() == DataSourceType.MYSQL,
        "The data source type is expected to be 'MYSQL'.");
  }
}