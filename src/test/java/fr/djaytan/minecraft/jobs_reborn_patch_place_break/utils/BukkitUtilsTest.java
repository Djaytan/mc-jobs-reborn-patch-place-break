package fr.djaytan.minecraft.jobs_reborn_patch_place_break.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BukkitUtilsTest {

  private BukkitUtils bukkitUtils;

  @BeforeEach
  void setUp() {
    bukkitUtils = new BukkitUtils();
  }

  @DisplayName("Conversion from a north direction")
  @Test
  void shouldReturnNorthDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.NORTH;

    // When
    Vector vector = bukkitUtils.convertToDirection(blockFace);

    // Then
    Vector expectedVector = new Vector(0, 0, -1);
    assertEquals(expectedVector, vector);
  }

  @DisplayName("Conversion from a south-east direction")
  @Test
  void shouldReturnSouthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SOUTH_EAST;

    // When
    Vector vector = bukkitUtils.convertToDirection(blockFace);

    // Then
    Vector expectedVector = new Vector(1, 0, 1);
    assertEquals(expectedVector, vector);
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnEastNorthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.EAST_NORTH_EAST;

    // When
    Vector vector = bukkitUtils.convertToDirection(blockFace);

    // Then
    Vector expectedVector = new Vector(2, 0, -1);
    assertEquals(expectedVector, vector);
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnSelfDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SELF;

    // When
    Vector vector = bukkitUtils.convertToDirection(blockFace);

    // Then
    Vector expectedVector = new Vector(0, 0, 0);
    assertEquals(expectedVector, vector);
  }
}
