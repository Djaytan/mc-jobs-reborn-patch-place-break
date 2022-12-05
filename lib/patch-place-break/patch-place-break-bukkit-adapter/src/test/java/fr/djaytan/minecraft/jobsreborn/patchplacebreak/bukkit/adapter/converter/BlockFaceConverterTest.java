package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.TagVector;

class BlockFaceConverterTest {

  private BlockFaceConverter blockFaceConverter;

  @BeforeEach
  void setUp() {
    blockFaceConverter = new BlockFaceConverter();
  }

  @DisplayName("Conversion from a north direction")
  @Test
  void shouldReturnNorthDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.NORTH;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(0, 0, -1);
    assertEquals(expectedTagVector, tagVector);
  }

  @DisplayName("Conversion from a south-east direction")
  @Test
  void shouldReturnSouthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SOUTH_EAST;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(1, 0, 1);
    assertEquals(expectedTagVector, tagVector);
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnEastNorthEastDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.EAST_NORTH_EAST;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(2, 0, -1);
    assertEquals(expectedTagVector, tagVector);
  }

  @DisplayName("Conversion from a self-oriented direction")
  @Test
  void shouldReturnSelfDirectionEquivalentVector() {
    // Given
    BlockFace blockFace = BlockFace.SELF;

    // When
    TagVector tagVector = blockFaceConverter.convert(blockFace);

    // Then
    TagVector expectedTagVector = TagVector.of(0, 0, 0);
    assertEquals(expectedTagVector, tagVector);
  }
}
