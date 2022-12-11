/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagLocation;

@ExtendWith(MockitoExtension.class)
class LocationConverterTest {

  private LocationConverter locationConverter;

  @BeforeEach
  void setUp() {
    locationConverter = new LocationConverter();
  }

  @Test
  @DisplayName("convert() - Successful nominal case")
  void shouldSuccessNominalLocationConversion(@Mock World world) {
    // Given
    String worldName = "world";
    when(world.getName()).thenReturn(worldName);

    double x = -96.0D;
    double y = 5.7788D;
    double z = -7854.55D;
    Location location = new Location(world, x, y, z);

    // When
    TagLocation tagLocation = locationConverter.convert(location);

    // Then
    TagLocation expectedTagLocation = TagLocation.of(worldName, x, y, z);
    assertEquals(expectedTagLocation, tagLocation);
  }

  @Test
  @DisplayName("convert() - Fail when Bukkit world value is null")
  void shouldFailLocationConversionWithNullBukkitWorld() {
    // Given
    double x = -96.0D;
    double y = 5.7788D;
    double z = -7854.55D;
    Location location = new Location(null, x, y, z);

    // When
    Executable executable = () -> locationConverter.convert(location);

    // Then
    assertThrowsExactly(NullPointerException.class, executable);
  }
}
