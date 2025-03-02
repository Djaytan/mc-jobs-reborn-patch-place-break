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
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerCredentialsConfigPropertiesTestDataSet.NOMINAL_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.DbmsServerCredentialsConfigPropertiesTestDataSet.NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerCredentialsPropertiesTestDataSet.NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerCredentialsPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PASSWORD;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerCredentialsPropertiesTestDataSet.NOMINAL_DBMS_SERVER_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class DbmsServerCredentialsConfigPropertiesTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues() {
      assertThat(
              new DbmsServerCredentialsConfigProperties(
                  NOMINAL_DBMS_SERVER_USERNAME, NOMINAL_DBMS_SERVER_PASSWORD))
          .satisfies(v -> assertThat(v.username()).isEqualTo(NOMINAL_DBMS_SERVER_USERNAME))
          .satisfies(v -> assertThat(v.password()).isEqualTo(NOMINAL_DBMS_SERVER_PASSWORD));
    }

    @Test
    void fromNominalModel() {
      assertThat(
              DbmsServerCredentialsConfigProperties.fromModel(
                  NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES))
          .isEqualTo(NOMINAL_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES);
    }
  }

  @Nested
  class WhenConvertingToModel {

    @Test
    void nominalCase() {
      assertThat(NOMINAL_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES.toModel())
          .isEqualTo(NOMINAL_DBMS_SERVER_CREDENTIALS_PROPERTIES);
    }

    @Test
    void fromDtoWithInvalidValue_shallFail() {
      var dbmsServerCredentialsPropertiesDto =
          new DbmsServerCredentialsConfigProperties(" ", NOMINAL_DBMS_SERVER_PASSWORD);

      assertThatThrownBy(dbmsServerCredentialsPropertiesDto::toModel)
          .isExactlyInstanceOf(IllegalArgumentException.class)
          .hasMessage("The DBMS server username cannot be blank")
          .hasNoCause();
    }
  }

  @Nested
  class WhenSerializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(serialize(NOMINAL_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES))
          .endsWith(NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  NOMINAL_SERIALIZED_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES,
                  DbmsServerCredentialsConfigProperties.class))
          .isEqualTo(NOMINAL_DBMS_SERVER_CREDENTIALS_CONFIG_PROPERTIES);
    }

    @Nested
    class ShallFailWhenMissingProperty {

      @Test
      void username() {
        assertDeserializationFailure(
            """
            password=test-password
            """,
            DbmsServerCredentialsConfigProperties.class);
      }

      @Test
      void password() {
        assertDeserializationFailure(
            """
            username=test-username
            """,
            DbmsServerCredentialsConfigProperties.class);
      }
    }
  }
}
