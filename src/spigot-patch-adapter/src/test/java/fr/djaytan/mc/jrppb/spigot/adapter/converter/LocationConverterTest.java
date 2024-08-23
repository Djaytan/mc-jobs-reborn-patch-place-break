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
package fr.djaytan.mc.jrppb.spigot.adapter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationConverterTest {

  private LocationConverter locationConverter;

  @BeforeEach
  void setUp() {
    locationConverter = new LocationConverter();
  }

  @Nested
  class WhenConverting {

    @Test
    void fromNominalBlock_shouldReturnExpectedBlockLocation(
        @Mock @NotNull Block block, @Mock @NotNull World world) {
      // Given
      String worldName = "world";
      given(world.getName()).willReturn(worldName);

      int x = -11;
      int y = 69;
      int z = 19;
      given(block.getX()).willReturn(x);
      given(block.getY()).willReturn(y);
      given(block.getZ()).willReturn(z);
      given(block.getWorld()).willReturn(world);

      // When
      BlockLocation blockLocation = locationConverter.convert(block);

      // Then
      assertThat(blockLocation).isEqualTo(new BlockLocation(worldName, x, y, z));
    }

    @Test
    void fromLocationWithoutWorldValue_shouldThrowException(@Mock @NotNull Block block) {
      // Given
      given(block.getWorld()).willReturn(null);

      // When
      Exception exception = catchException(() -> locationConverter.convert(block));

      // Then
      assertThat(exception)
          .isExactlyInstanceOf(NullPointerException.class)
          .hasMessage("The associated world to a Bukkit block can't be null");
    }
  }
}
