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
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.ConnectionPoolPropertiesDtoTestDataSet.NOMINAL_CONNECTION_POOL_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.ConnectionPoolPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_CONNECTION_POOL_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_POOL_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_TIMEOUT;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_POOL_SIZE;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.randomInvalidConnectionTimeout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class ConnectionPoolPropertiesDtoTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues() {
      assertThat(new ConnectionPoolPropertiesDto(NOMINAL_CONNECTION_TIMEOUT, NOMINAL_POOL_SIZE))
          .satisfies(v -> assertThat(v.connectionTimeout()).isEqualTo(NOMINAL_CONNECTION_TIMEOUT))
          .satisfies(v -> assertThat(v.poolSize()).isEqualTo(NOMINAL_POOL_SIZE));
    }

    @Test
    void fromNominalModel() {
      assertThat(ConnectionPoolPropertiesDto.fromModel(NOMINAL_CONNECTION_POOL_PROPERTIES))
          .isEqualTo(NOMINAL_CONNECTION_POOL_PROPERTIES_DTO);
    }
  }

  @Nested
  class WhenConvertingToModel {

    @Test
    void nominalCase() {
      assertThat(NOMINAL_CONNECTION_POOL_PROPERTIES_DTO.toModel())
          .isEqualTo(NOMINAL_CONNECTION_POOL_PROPERTIES);
    }

    @Test
    void fromDtoWithInvalidValue_shallFail() {
      var connectionPoolPropertiesDto =
          new ConnectionPoolPropertiesDto(randomInvalidConnectionTimeout(), NOMINAL_POOL_SIZE);

      assertThatThrownBy(connectionPoolPropertiesDto::toModel)
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("The connection timeout must be between 1 and 600000")
          .hasNoCause();
    }
  }

  @Nested
  class WhenSerializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(serialize(NOMINAL_CONNECTION_POOL_PROPERTIES_DTO))
          .endsWith(NOMINAL_SERIALIZED_CONNECTION_POOL_PROPERTIES);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  NOMINAL_SERIALIZED_CONNECTION_POOL_PROPERTIES, ConnectionPoolPropertiesDto.class))
          .isEqualTo(NOMINAL_CONNECTION_POOL_PROPERTIES_DTO);
    }

    @Nested
    class ShallFailWhenMissingProperty {

      @Test
      void connectionTimeout() {
        assertDeserializationFailure(
            """
            poolSize=20
            """, ConnectionPoolPropertiesDto.class);
      }

      @Test
      void poolSize() {
        assertDeserializationFailure(
            """
            connectionTimeout=20000
            """,
            ConnectionPoolPropertiesDto.class);
      }
    }
  }
}
