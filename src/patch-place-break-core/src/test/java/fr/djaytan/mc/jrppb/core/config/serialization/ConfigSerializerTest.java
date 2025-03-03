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
package fr.djaytan.mc.jrppb.core.config.serialization;

import static fr.djaytan.mc.jrppb.core.config.properties.AnotherConfigProperties.ANOTHER_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.properties.NominalConfigProperties.NOMINAL_CONFIG_PROPERTIES;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializer.deserialize;
import static fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializer.serialize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.djaytan.mc.jrppb.core.config.properties.AnotherConfigProperties;
import fr.djaytan.mc.jrppb.core.config.properties.NominalConfigProperties;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.serialize.SerializationException;

final class ConfigSerializerTest {

  private static final String NOMINAL_SERIALIZED_CONFIG_PROPERTIES =
      """
      #         JobsReborn-PatchPlaceBreak
      # A patch place-break extension for JobsReborn
      #                (by Djaytan)
      #\s
      # This config file use HOCON format
      # Specifications are here: https://github.com/lightbend/config/blob/main/HOCON.md
      #\s
      # /!\\ Properties ordering is nondeterministic at config generation time because of limitations
      # of underlying library.

      # Multi
      # line
      # comment
      number=34
      # Single line comment
      testField=test-value
      """;
  private static final String ANOTHER_SERIALIZED_CONFIG_PROPERTIES =
      """
      #         JobsReborn-PatchPlaceBreak
      # A patch place-break extension for JobsReborn
      #                (by Djaytan)
      #\s
      # This config file use HOCON format
      # Specifications are here: https://github.com/lightbend/config/blob/main/HOCON.md
      #\s
      # /!\\ Properties ordering is nondeterministic at config generation time because of limitations
      # of underlying library.

      test=another
      """;

  @Nested
  class WhenSerializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(serialize(NOMINAL_CONFIG_PROPERTIES))
          .isEqualToIgnoringNewLines(NOMINAL_SERIALIZED_CONFIG_PROPERTIES);
    }

    @Test
    void withAnotherConfigProperties_shallSucceed() {
      assertThat(serialize(ANOTHER_CONFIG_PROPERTIES))
          .isEqualToIgnoringNewLines(ANOTHER_SERIALIZED_CONFIG_PROPERTIES);
    }

    @Test
    void withNotSerializableObject_shallFail() {
      assertThatThrownBy(() -> serialize(new NotSerializable("test")))
          .isExactlyInstanceOf(ConfigSerializationException.class)
          .hasMessage(
              "Fail to serialize the following config properties: NotSerializable[dummy=test]")
          .cause()
          .isExactlyInstanceOf(SerializationException.class)
          .hasMessageEndingWith(
              "No serializer available for type class "
                  + "fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializerTest$NotSerializable");
    }
  }

  @Nested
  class WhenDeserializing {

    @Test
    void nominalCase() throws ConfigSerializationException {
      assertThat(deserialize(NOMINAL_SERIALIZED_CONFIG_PROPERTIES, NominalConfigProperties.class))
          .isEqualTo(NOMINAL_CONFIG_PROPERTIES);
    }

    @Test
    void fromAnotherSerializedConfigProperties_shallSucceed() {
      assertThat(deserialize(ANOTHER_SERIALIZED_CONFIG_PROPERTIES, AnotherConfigProperties.class))
          .isEqualTo(ANOTHER_CONFIG_PROPERTIES);
    }

    @Test
    void withoutHeader_shallSucceed() throws ConfigSerializationException {
      assertThat(
              deserialize(
                  """
                  # Multi
                  # line
                  # comment
                  number=34
                  # Single line comment
                  testField=test-value""",
                  NominalConfigProperties.class))
          .isEqualTo(NOMINAL_CONFIG_PROPERTIES);
    }

    @Test
    void withNotDeserializableTargetType_shallFail() {
      assertThatThrownBy(() -> deserialize("dummy=test", NotSerializable.class))
          .isExactlyInstanceOf(ConfigSerializationException.class)
          .hasMessage("Unexpectedly deserialized the following input to null:\ndummy=test")
          .hasNoCause();
    }

    @Test
    void withMissingRequiredField_shallFail() {
      assertThatThrownBy(() -> deserialize("number=120", NominalConfigProperties.class))
          .isExactlyInstanceOf(ConfigSerializationException.class)
          .hasMessage(
              "Fail to deserialize config properties of type "
                  + "'fr.djaytan.mc.jrppb.core.config.properties.NominalConfigProperties' "
                  + "from the following config input:\nnumber=120")
          .cause()
          .isExactlyInstanceOf(SerializationException.class)
          .hasMessage("[testField] of type java.lang.String: A value is required for this field");
    }

    @Test
    void withNonCamelCaseFieldName_shallFail() {
      assertThatThrownBy(
              () ->
                  deserialize(
                      """
                      number=34
                      test_field=test-value""",
                      NominalConfigProperties.class))
          .isExactlyInstanceOf(ConfigSerializationException.class)
          .cause()
          .isExactlyInstanceOf(SerializationException.class)
          .hasMessage("[testField] of type java.lang.String: A value is required for this field");
    }
  }

  @Nested
  class WhenRoundTripping {

    @Nested
    class FromNominalSerializedProperties {

      @Test
      void oneRoundTrip() throws ConfigSerializationException {
        String roundTripResult =
            serialize(
                deserialize(NOMINAL_SERIALIZED_CONFIG_PROPERTIES, NominalConfigProperties.class));

        assertThat(roundTripResult).isEqualToIgnoringNewLines(NOMINAL_SERIALIZED_CONFIG_PROPERTIES);
      }

      @Test
      void multipleRoundTrips() throws ConfigSerializationException {
        String roundTripResult =
            serialize(
                deserialize(
                    serialize(
                        deserialize(
                            NOMINAL_SERIALIZED_CONFIG_PROPERTIES, NominalConfigProperties.class)),
                    NominalConfigProperties.class));

        assertThat(roundTripResult).isEqualToIgnoringNewLines(NOMINAL_SERIALIZED_CONFIG_PROPERTIES);
      }
    }

    @Nested
    class FromNominalDeserializedProperties {

      @Test
      void oneRoundTrip() throws ConfigSerializationException {
        NominalConfigProperties roundTripResult =
            deserialize(serialize(NOMINAL_CONFIG_PROPERTIES), NominalConfigProperties.class);

        assertThat(roundTripResult).isEqualTo(NOMINAL_CONFIG_PROPERTIES);
      }

      @Test
      void multipleRoundTrips() throws ConfigSerializationException {
        NominalConfigProperties roundTripResult =
            deserialize(
                serialize(
                    deserialize(
                        serialize(NOMINAL_CONFIG_PROPERTIES), NominalConfigProperties.class)),
                NominalConfigProperties.class);

        assertThat(roundTripResult).isEqualTo(NOMINAL_CONFIG_PROPERTIES);
      }
    }
  }

  private record NotSerializable(@NotNull String dummy) {}
}
