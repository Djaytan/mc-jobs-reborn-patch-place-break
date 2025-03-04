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
package fr.djaytan.mc.jrppb.core.config.properties;

import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerHostConfigPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.DbmsServerHostConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_HOST_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializer.deserialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializer.serialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerAssertions.assertDeserializationFailure;
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

final class DbmsServerHostConfigPropertiesTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues() {
      assertThat(
              new DbmsServerHostConfigProperties(
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
      assertThat(DbmsServerHostConfigProperties.fromModel(NOMINAL_DBMS_SERVER_HOST_PROPERTIES))
          .isEqualTo(NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES);
    }
  }

  @Nested
  class WhenConvertingToModel {

    @Test
    void nominalCase() {
      assertThat(NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES.toModel())
          .isEqualTo(NOMINAL_DBMS_SERVER_HOST_PROPERTIES);
    }

    @Test
    void fromDtoWithInvalidValues_shallFail() {
      var dbmsServerHostPropertiesDto =
          new DbmsServerHostConfigProperties(
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
      assertThat(serialize(NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES))
          .endsWith(NOMINAL_SERIALIZED_DBMS_SERVER_HOST_CONFIG_PROPERTIES);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  NOMINAL_SERIALIZED_DBMS_SERVER_HOST_CONFIG_PROPERTIES,
                  DbmsServerHostConfigProperties.class))
          .isEqualTo(NOMINAL_DBMS_SERVER_HOST_CONFIG_PROPERTIES);
    }

    @Nested
    class ShallFailWhenMissingProperty {

      @Test
      void hostname() {
        assertDeserializationFailure(
            """
            port=4235
            is-ssl-enabled=true
            """,
            DbmsServerHostConfigProperties.class);
      }

      @Test
      void port() {
        assertDeserializationFailure(
            """
            hostname=hostname.com
            is-ssl-enabled=true
            """,
            DbmsServerHostConfigProperties.class);
      }

      @Test
      void isSslEnabled() {
        assertDeserializationFailure(
            """
            hostname=hostname.com
            port=4235
            """,
            DbmsServerHostConfigProperties.class);
      }
    }
  }
}
