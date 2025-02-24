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

import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST;
import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_RESTRICTED_BLOCKS_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_RESTRICTION_MODE;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.deserialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2.serialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerV2Assertions.assertDeserializationFailure;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.RestrictedBlocksPropertiesDtoTestDataSet.NOMINAL_RESTRICTED_BLOCKS_PROPERTIES_DTO;
import static fr.djaytan.mc.jrppb.core.config.serialization.properties.RestrictedBlocksPropertiesDtoTestDataSet.NOMINAL_SERIALIZED_RESTRICTED_BLOCKS_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.serialize.SerializationException;

final class RestrictedBlocksPropertiesDtoTest {

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues() {
      assertThat(
              new RestrictedBlocksPropertiesDto(
                  NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST, NOMINAL_RESTRICTION_MODE))
          .satisfies(
              v -> assertThat(v.materials()).isEqualTo(NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST))
          .satisfies(v -> assertThat(v.restrictionMode()).isEqualTo(NOMINAL_RESTRICTION_MODE));
    }

    @Test
    void fromNominalValues() {
      assertThat(RestrictedBlocksPropertiesDto.fromModel(NOMINAL_RESTRICTED_BLOCKS_PROPERTIES))
          .isEqualTo(NOMINAL_RESTRICTED_BLOCKS_PROPERTIES_DTO);
    }
  }

  @Nested
  class WhenConvertingToModel {

    @Test
    void nominalCase() {
      assertThat(NOMINAL_RESTRICTED_BLOCKS_PROPERTIES_DTO.toModel())
          .isEqualTo(NOMINAL_RESTRICTED_BLOCKS_PROPERTIES);
    }
  }

  @Nested
  class WhenSerializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(serialize(NOMINAL_RESTRICTED_BLOCKS_PROPERTIES_DTO))
          .endsWith(NOMINAL_SERIALIZED_RESTRICTED_BLOCKS_PROPERTIES);
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  NOMINAL_SERIALIZED_RESTRICTED_BLOCKS_PROPERTIES,
                  RestrictedBlocksPropertiesDto.class))
          .isEqualTo(NOMINAL_RESTRICTED_BLOCKS_PROPERTIES_DTO);
    }

    @Test
    void whenListOfMaterialsIsEmpty_shallSucceed() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  """
                  materials=[]
                  restrictionMode=BLACKLIST""",
                  RestrictedBlocksPropertiesDto.class))
          .satisfies(v -> assertThat(v.materials()).isEmpty());
    }

    @Test
    void whenRestrictionModeIsInvalid_shallFail() {
      assertThatThrownBy(
              () ->
                  deserialize(
                      """
                  materials=[]
                  restrictionMode=INVALID""",
                      RestrictedBlocksPropertiesDto.class))
          .isExactlyInstanceOf(ConfigSerializationException.class)
          .cause()
          .isExactlyInstanceOf(SerializationException.class)
          .hasMessage(
              "[restrictionMode] of type fr.djaytan.mc.jrppb.core.RestrictionMode: "
                  + "Invalid enum constant provided, expected a value of enum, got INVALID");
    }

    @Nested
    class ShallFailWhenMissingProperty {

      @Test
      void materials() {
        assertDeserializationFailure(
            "restrictionMode=BLACKLIST", RestrictedBlocksPropertiesDto.class);
      }

      @Test
      void restrictionMode() {
        assertDeserializationFailure(
            """
            materials=[
                DIRT,
                SAND,
                STONE
            ]""",
            RestrictedBlocksPropertiesDto.class);
      }
    }
  }
}
