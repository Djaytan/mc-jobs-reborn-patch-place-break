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
package fr.djaytan.mc.jrppb.core.config.properties_v2;

import static fr.djaytan.mc.jrppb.core.config.properties_v2.ConnectionPoolConfigPropertiesTestDataSet.NOMINAL_CONNECTION_POOL_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties_v2.ConnectionPoolConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_CONNECTION_POOL_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties_v2.DataSourceConfigPropertiesTestDataSet.NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties_v2.DataSourceConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_DATA_SOURCE_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties_v2.DbmsServerConfigPropertiesTestDataSet.NOMINAL_DBMS_SERVER_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties_v2.DbmsServerConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.deserialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.serialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2Assertions.assertDeserializationFailure;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_DATA_SOURCE_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_DATA_SOURCE_TABLE_NAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DataSourcePropertiesTestDataSet.NOMINAL_DATA_SOURCE_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class DataSourceConfigPropertiesTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues() {
      assertThat(
              new DataSourceConfigProperties(
                  NOMINAL_DATA_SOURCE_TYPE,
                  NOMINAL_DATA_SOURCE_TABLE_NAME,
                  NOMINAL_DBMS_SERVER_CONFIG_PROPERTIES,
                  NOMINAL_CONNECTION_POOL_CONFIG_PROPERTIES))
          .satisfies(v -> assertThat(v.type()).isEqualTo(NOMINAL_DATA_SOURCE_TYPE))
          .satisfies(v -> assertThat(v.table()).isEqualTo(NOMINAL_DATA_SOURCE_TABLE_NAME))
          .satisfies(
              v -> assertThat(v.dbmsServer()).isEqualTo(NOMINAL_DBMS_SERVER_CONFIG_PROPERTIES))
          .satisfies(
              v ->
                  assertThat(v.connectionPool())
                      .isEqualTo(NOMINAL_CONNECTION_POOL_CONFIG_PROPERTIES));
    }

    @Test
    void fromNominalModel() {
      assertThat(DataSourceConfigProperties.fromModel(NOMINAL_DATA_SOURCE_PROPERTIES))
          .isEqualTo(NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES);
    }
  }

  @Nested
  class WhenConvertingToModel {

    @Test
    void fromNominalDto() {
      assertThat(NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES.toModel())
          .isEqualTo(NOMINAL_DATA_SOURCE_PROPERTIES);
    }

    @Test
    void fromDtoWithInvalidValue_shallFail() {
      var dataSourcePropertiesDto =
          new DataSourceConfigProperties(
              NOMINAL_DATA_SOURCE_TYPE,
              " ",
              NOMINAL_DBMS_SERVER_CONFIG_PROPERTIES,
              NOMINAL_CONNECTION_POOL_CONFIG_PROPERTIES);

      assertThatThrownBy(dataSourcePropertiesDto::toModel)
          .isExactlyInstanceOf(IllegalArgumentException.class)
          .hasMessage("The data source table name must not be blank")
          .hasNoCause();
    }
  }

  @Nested
  class WhenSerializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(serialize(NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES))
          .endsWith(NOMINAL_SERIALIZED_DATA_SOURCE_CONFIG_PROPERTIES);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  NOMINAL_SERIALIZED_DATA_SOURCE_CONFIG_PROPERTIES,
                  DataSourceConfigProperties.class))
          .isEqualTo(NOMINAL_DATA_SOURCE_CONFIG_PROPERTIES);
    }

    @Nested
    class ShallFailWhenMissingProperty {

      @Test
      void type() {
        assertDeserializationFailure(
            """
            connectionPool {
                %s
            }
            dbmsServer {
                %s
            }
            table="nominal_table_name"
            """
                .formatted(
                    NOMINAL_SERIALIZED_CONNECTION_POOL_CONFIG_PROPERTIES.indent(4).trim(),
                    NOMINAL_SERIALIZED_DBMS_SERVER_CONFIG_PROPERTIES.indent(4).trim()),
            ConnectionPoolConfigProperties.class);
      }

      @Test
      void table() {
        assertDeserializationFailure(
            """
            connectionPool {
                %s
            }
            dbmsServer {
                %s
            }
            type=MYSQL
            """
                .formatted(
                    NOMINAL_SERIALIZED_CONNECTION_POOL_CONFIG_PROPERTIES.indent(4).trim(),
                    NOMINAL_SERIALIZED_DBMS_SERVER_CONFIG_PROPERTIES.indent(4).trim()),
            ConnectionPoolConfigProperties.class);
      }

      @Test
      void dbmsServer() {
        assertDeserializationFailure(
            """
            connectionPool {
                %s
            }
            table="nominal_table_name"
            type=MYSQL
            """
                .formatted(NOMINAL_SERIALIZED_CONNECTION_POOL_CONFIG_PROPERTIES.indent(4).trim()),
            ConnectionPoolConfigProperties.class);
      }

      @Test
      void connectionPool() {
        assertDeserializationFailure(
            """
            dbmsServer {
                %s
            }
            table="nominal_table_name"
            type=MYSQL
            """
                .formatted(NOMINAL_SERIALIZED_DBMS_SERVER_CONFIG_PROPERTIES.indent(4).trim()),
            ConnectionPoolConfigProperties.class);
      }
    }
  }
}
