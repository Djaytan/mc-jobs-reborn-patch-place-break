package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import static org.assertj.core.api.Assertions.assertThat;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlockFaceConverterTest {

  private BlockFaceConverter blockFaceConverter;

  @BeforeEach
  void beforeEach() {
    blockFaceConverter = new BlockFaceConverter();
  }

  @Test
  @DisplayName("Conversion from a north direction")
  void shouldReturnNorthDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.NORTH;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(0, 0, -1));
  }

  @Test
  @DisplayName("Conversion from a south-east direction")
  void shouldReturnSouthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SOUTH_EAST;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(1, 0, 1));
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnEastNorthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.EAST_NORTH_EAST;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(2, 0, -1));
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnSelfDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SELF;

    // When
    Vector vector = blockFaceConverter.convert(blockFace);

    // Then
    assertThat(vector).isEqualTo(Vector.of(0, 0, 0));
  }
}
