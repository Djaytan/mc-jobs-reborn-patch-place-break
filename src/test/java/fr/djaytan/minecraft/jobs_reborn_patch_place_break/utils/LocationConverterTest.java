package fr.djaytan.minecraft.jobs_reborn_patch_place_break.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity.TagLocation;

@ExtendWith(MockitoExtension.class)
public class LocationConverterTest {

  private LocationConverter locationConverter;

  @BeforeEach
  public void setUp() {
    locationConverter = new LocationConverter();
  }

  /**
   * Given a Bukkit location,
   * When converting it to a Tag location,
   * Then the conversion should occur successfully.
   *
   * @param world The Bukkit world mock.
   */
  @Test
  @DisplayName("convert() - Successful nominal case")
  void shouldSuccessNominalLocationConversion(@Mock World world) {
    //
    // Given
    //
    String worldName = "world";
    when(world.getName()).thenReturn(worldName);

    double x = -96.0D;
    double y = 5.7788D;
    double z = -7854.55D;
    Location location = new Location(world, x, y, z);

    //
    // When
    //
    TagLocation tagLocation = locationConverter.convert(location);

    //
    // Then
    //
    TagLocation expectedTagLocation = TagLocation.of(worldName, x, y, z);
    assertEquals(expectedTagLocation, tagLocation);
  }

  /**
   * Given a Bukkit location with a null Bukkit world,
   * When converting it to a Tag location,
   * Then a NullPointerException should be thrown.
   */
  @Test
  @DisplayName("convert() - Fail when Bukkit world value is null")
  void shouldFailLocationConversionWithNullBukkitWorld() {
    //
    // Given
    //
    double x = -96.0D;
    double y = 5.7788D;
    double z = -7854.55D;
    Location location = new Location(null, x, y, z);

    //
    // When & Then
    //
    assertThrowsExactly(NullPointerException.class, () -> locationConverter.convert(location));
  }
}
