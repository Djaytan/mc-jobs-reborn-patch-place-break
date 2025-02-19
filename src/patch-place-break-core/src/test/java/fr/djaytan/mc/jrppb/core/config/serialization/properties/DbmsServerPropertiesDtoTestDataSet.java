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
package fr.djaytan.mc.jrppb.core.config.serialization.properties;

import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerCredentialsPropertiesDtoTestDataSet.NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerCredentialsPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerHostPropertiesDtoTestDataSet.NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerHostPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_DATABASE_NAME;

public final class DbmsServerPropertiesDtoTestDataSet {

  public static final DbmsServerPropertiesDto NOMINAL_DBMS_SERVER_PROPERTIES_DTO =
      new DbmsServerPropertiesDto(
          NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO,
          NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES_DTO,
          NOMINAL_DBMS_SERVER_DATABASE_NAME);

  public static final String NOMINAL_SERIALIZED_DBMS_SERVER_PROPERTIES =
      """
      # Credentials for authentication with the DBMS server
      credentials {
          %s
      }
      # The database to use on DBMS server
      # Value can't be empty or blank
      database=minecraft
      # Host properties of the DBMS server
      host {
          %s
      }
      """
          .formatted(
              NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_PROPERTIES.indent(4).trim(),
              NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES.indent(4).trim());
}
