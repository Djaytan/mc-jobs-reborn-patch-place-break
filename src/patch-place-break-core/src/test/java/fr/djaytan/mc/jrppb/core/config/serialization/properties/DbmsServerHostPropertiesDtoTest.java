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
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerHostPropertiesDtoTestDataSet.NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerHostPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOSTNAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOST_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_IS_SSL_ENABLED;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PORT;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.randomTooLongDbmsServerHostname;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class DbmsServerHostPropertiesDtoTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues() {
      assertThat(
              new DbmsServerHostPropertiesDto(
                  NOMINAL_DBMS_SERVER_HOSTNAME,
                  NOMINAL_DBMS_SERVER_PORT,
                  NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
          .satisfies(v -> assertThat(v.hostname()).isEqualTo(NOMINAL_DBMS_SERVER_HOSTNAME))
          .satisfies(v -> assertThat(v.port()).isEqualTo(NOMINAL_DBMS_SERVER_PORT))
          .satisfies(
              v -> assertThat(v.isSslEnabled()).isEqualTo(NOMINAL_DBMS_SERVER_IS_SSL_ENABLED));
    }

    @Test
    void fromNominalModel() {
      assertThat(DbmsServerHostPropertiesDto.fromModel(NOMINAL_DBMS_SERVER_HOST_PROPERTIES))
          .isEqualTo(NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO);
    }
  }

  @Nested
  class WhenConvertingToModel {

    @Test
    void nominalCase() {
      assertThat(NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO.toModel())
          .isEqualTo(NOMINAL_DBMS_SERVER_HOST_PROPERTIES);
    }

    @Test
    void fromDtoWithInvalidValues_shallFail() {
      var dbmsServerHostPropertiesDto =
          new DbmsServerHostPropertiesDto(
              randomTooLongDbmsServerHostname(),
              NOMINAL_DBMS_SERVER_PORT,
              NOMINAL_DBMS_SERVER_IS_SSL_ENABLED);

      assertThatThrownBy(dbmsServerHostPropertiesDto::toModel)
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("The DBMS server hostname cannot exceed 255 characters")
          .hasNoCause();
    }
  }

  @Nested
  class WhenSerializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(serialize(NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO))
          .endsWith(NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  NOMINAL_SERIALIZED_DBMS_SERVER_HOST_PROPERTIES,
                  DbmsServerHostPropertiesDto.class))
          .isEqualTo(NOMINAL_DBMS_SERVER_HOST_PROPERTIES_DTO);
    }

    @Nested
    class ShallFailWhenMissingProperty {

      @Test
      void hostname() {
        assertDeserializationFailure(
            """
            is-ssl-enabled=true
            port=4235
            """,
            DbmsServerHostPropertiesDto.class);
      }

      @Test
      void port() {
        assertDeserializationFailure(
            """
            hostname=hostname.com
            is-ssl-enabled=true
            """,
            DbmsServerHostPropertiesDto.class);
      }

      @Test
      void isSslEnabled() {
        assertDeserializationFailure(
            """
            hostname=hostname.com
            port=4235
            """,
            DbmsServerHostPropertiesDto.class);
      }
    }
  }
}
