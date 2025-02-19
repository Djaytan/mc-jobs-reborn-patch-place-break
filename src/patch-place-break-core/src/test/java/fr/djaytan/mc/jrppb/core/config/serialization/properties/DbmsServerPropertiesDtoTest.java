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

import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.deserialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.serialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2Assertions.assertDeserializationFailure;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerCredentialsPropertiesDtoTestDataSet.NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerCredentialsPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerHostPropertiesDtoTestDataSet.NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerHostPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerPropertiesDtoTestDataSet.NOMINAL_DBMS_SERVER_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_DATABASE_NAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class DbmsServerPropertiesDtoTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues() {
      assertThat(
              new DbmsServerPropertiesDto(
                  NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO,
                  NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES_DTO,
                  NOMINAL_DBMS_SERVER_DATABASE_NAME))
          .satisfies(v -> assertThat(v.host()).isEqualTo(NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO))
          .satisfies(
              v ->
                  assertThat(v.credentials())
                      .isEqualTo(NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES_DTO))
          .satisfies(v -> assertThat(v.database()).isEqualTo(NOMINAL_DBMS_SERVER_DATABASE_NAME));
    }

    @Test
    void fromNominalModel() {
      assertThat(DbmsServerPropertiesDto.fromModel(NOMINAL_DBMS_SERVER_PROPERTIES))
          .isEqualTo(NOMINAL_DBMS_SERVER_PROPERTIES_DTO);
    }
  }

  @Nested
  class WhenConvertingToModel {

    @Test
    void fromNominalDto_shallSucceed() {
      assertThat(NOMINAL_DBMS_SERVER_PROPERTIES_DTO.toModel())
          .isEqualTo(NOMINAL_DBMS_SERVER_PROPERTIES);
    }

    @Test
    void fromDtoWithInvalidValues_shallFail() {
      var dbmsServerPropertiesDto =
          new DbmsServerPropertiesDto(
              NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO,
              NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES_DTO,
              " ");

      assertThatThrownBy(dbmsServerPropertiesDto::toModel)
          .isExactlyInstanceOf(IllegalArgumentException.class)
          .hasMessage("The DBMS server database name cannot be blank")
          .hasNoCause();
    }
  }

  @Nested
  class WhenSerializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(serialize(NOMINAL_DBMS_SERVER_PROPERTIES_DTO))
          .endsWith(NOMINAL_SERIALIZED_DBMS_SERVER_PROPERTIES);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(
              deserialize(NOMINAL_SERIALIZED_DBMS_SERVER_PROPERTIES, DbmsServerPropertiesDto.class))
          .isEqualTo(NOMINAL_DBMS_SERVER_PROPERTIES_DTO);
    }

    @Nested
    class ShallFailWhenMissingProperty {

      @Test
      void host() {
        assertDeserializationFailure(
            """
            credentials {
                %s
            }
            database=minecraft
            """
                .formatted(NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_PROPERTIES.indent(4).trim()),
            ConnectionPoolPropertiesDto.class);
      }

      @Test
      void credentials() {
        assertDeserializationFailure(
            """
            database=minecraft
            host {
                %s
            }
            """
                .formatted(NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES.indent(4).trim()),
            ConnectionPoolPropertiesDto.class);
      }

      @Test
      void database() {
        assertDeserializationFailure(
            """
          credentials {
              %s
          }
          host {
              %s
          }
          """
                .formatted(
                    NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_PROPERTIES.indent(4).trim(),
                    NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES.indent(4).trim()),
            ConnectionPoolPropertiesDto.class);
      }
    }
  }
}
