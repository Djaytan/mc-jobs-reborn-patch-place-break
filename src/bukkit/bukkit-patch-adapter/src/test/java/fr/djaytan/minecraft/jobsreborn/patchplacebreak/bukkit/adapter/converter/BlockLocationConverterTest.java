package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.BDDMockito.given;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlockLocationConverterTest {

  private LocationConverter locationConverter;

  @BeforeEach
  void beforeEach() {
    locationConverter = new LocationConverter();
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("From nominal block")
    void fromNominalBlock(@Mock Block block, @Mock World world) {
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
      assertThat(blockLocation).isEqualTo(BlockLocation.of(worldName, x, y, z));
    }

    @Test
    @DisplayName("From block without world value")
    void fromLocationWithoutWorldValue(@Mock Block block) {
      // Given

      // When
      Executable executable = () -> locationConverter.convert(block);

      // Then
      assertThrowsExactly(NullPointerException.class, executable);
    }
  }
}
