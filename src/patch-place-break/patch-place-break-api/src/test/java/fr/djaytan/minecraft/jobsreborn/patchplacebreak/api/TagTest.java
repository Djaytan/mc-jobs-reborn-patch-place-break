/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.TagLocation;
import java.time.LocalDateTime;
import java.util.UUID;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    UUID uuid = UUID.randomUUID();
    LocalDateTime localDateTime = LocalDateTime.now();
    boolean isEphemeral = true;

    String worldName = "world";
    double x = 52.0D;
    double y = 68.1223D;
    double z = 1254.785D;
    TagLocation tagLocation = TagLocation.of(worldName, x, y, z);

    // When
    Tag tag = Tag.of(uuid, localDateTime, isEphemeral, tagLocation);

    // Then
    assertAll(
        "Verification of returned values from getters",
        () -> assertThat(tag.getUuid()).isEqualTo(uuid),
        () -> assertThat(tag.getInitLocalDateTime()).isEqualTo(localDateTime),
        () -> assertThat(tag.isEphemeral()).isEqualTo(isEphemeral),
        () -> assertThat(tag.getTagLocation()).isEqualTo(tagLocation));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(Tag.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(Tag.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
