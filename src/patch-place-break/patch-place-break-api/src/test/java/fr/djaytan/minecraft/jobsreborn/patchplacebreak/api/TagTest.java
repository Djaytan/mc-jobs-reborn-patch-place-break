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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import java.time.LocalDateTime;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class TagTest {

  @InjectSoftAssertions private SoftAssertions softly;

  @Test
  @DisplayName("When instantiating with nominal values")
  void whenInstantiatingWithNominalValues() {
    // Given
    String worldName = "world";
    int x = 52;
    int y = 68;
    int z = 1254;
    BlockLocation blockLocation = new BlockLocation(worldName, x, y, z);

    boolean isEphemeral = true;
    LocalDateTime localDateTime = LocalDateTime.now();

    // When
    Tag tag = new Tag(blockLocation, isEphemeral, localDateTime);

    // Then
    softly.assertThat(tag.getInitLocalDateTime()).isEqualTo(localDateTime);
    softly.assertThat(tag.isEphemeral()).isEqualTo(isEphemeral);
    softly.assertThat(tag.getBlockLocation()).isEqualTo(blockLocation);
  }

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(Tag.class).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(Tag.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
