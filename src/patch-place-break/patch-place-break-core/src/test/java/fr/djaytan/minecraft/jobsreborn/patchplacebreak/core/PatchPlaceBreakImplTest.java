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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Block;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Vector;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictedBlocksProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.properties.RestrictionMode;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties.RestrictedBlocksPropertiesImpl;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPair;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.OldNewBlockLocationPairSet;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.TagRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatchPlaceBreakImplTest {

  @Mock private TagRepository tagRepository;
  @Captor private ArgumentCaptor<Tag> tagCaptor;
  @Captor private ArgumentCaptor<OldNewBlockLocationPairSet> locationPairCaptor;
  @Captor private ArgumentCaptor<BlockLocation> blockLocationCaptor;
  private final Clock clock = Clock.systemUTC();
  private final RestrictedBlocksProperties restrictedBlocksProperties =
      new RestrictedBlocksPropertiesImpl(
          new HashSet<>(Arrays.asList("STONE", "DIRT")), RestrictionMode.BLACKLIST);
  private PatchPlaceBreakImpl patchPlaceBreakImpl;

  @BeforeEach
  void beforeEach() {
    BlocksFilter blocksFilter = new BlocksFilter(restrictedBlocksProperties);

    this.patchPlaceBreakImpl =
        new PatchPlaceBreakImpl(blocksFilter, clock, restrictedBlocksProperties, tagRepository);
  }

  @Nested
  @DisplayName("When put tag")
  class WhenPutTag {

    @Test
    @DisplayName("With not restricted block")
    void withNotRestrictedBlock() {
      // Given
      BlockLocation blockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToTag = new Block(blockLocation, "BEACON");

      // When
      patchPlaceBreakImpl.putTag(blockToTag, false).join();

      // Then
      verify(tagRepository).put(tagCaptor.capture());
      Tag tag = tagCaptor.getValue();

      assertAll(
          () -> assertThat(tag.getBlockLocation()).isEqualTo(blockLocation),
          () -> assertThat(tag.isEphemeral()).isFalse());
    }

    @Test
    @DisplayName("With restricted block")
    void withRestrictedBlock() {
      // Given
      BlockLocation blockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToTag = new Block(blockLocation, "STONE");

      // When
      patchPlaceBreakImpl.putTag(blockToTag, false).join();

      // Then
      verifyNoInteractions(tagRepository);
    }

    @Test
    @DisplayName("With ephemeral tag")
    void withEphemeralTag() {
      // Given
      BlockLocation blockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToTag = new Block(blockLocation, "BEACON");

      // When
      patchPlaceBreakImpl.putTag(blockToTag, true).join();

      // Then
      verify(tagRepository).put(tagCaptor.capture());
      Tag tag = tagCaptor.getValue();

      assertAll(
          () -> assertThat(tag.getBlockLocation()).isEqualTo(blockLocation),
          () -> assertThat(tag.isEphemeral()).isTrue());
    }
  }

  @Nested
  @DisplayName("When remove tag")
  class WhenRemoveTag {

    @Test
    @DisplayName("With not restricted block")
    void withNotRestrictedBlock() {
      // Given
      BlockLocation newBlockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToRemoveTag = new Block(newBlockLocation, "BEACON");

      // When
      patchPlaceBreakImpl.removeTag(blockToRemoveTag).join();

      // Then
      verify(tagRepository).delete(blockLocationCaptor.capture());
      BlockLocation capturedBlockLocation = blockLocationCaptor.getValue();
      assertThat(capturedBlockLocation).isEqualTo(newBlockLocation);
    }

    @Test
    @DisplayName("With restricted block")
    void withRestrictedBlock() {
      // Given
      BlockLocation blockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToTag = new Block(blockLocation, "STONE");

      // When
      patchPlaceBreakImpl.removeTag(blockToTag).join();

      // Then
      verifyNoInteractions(tagRepository);
    }
  }

  @Nested
  @DisplayName("When move tags")
  class WhenMoveTags {

    @Test
    @DisplayName("With only unrestricted blocks")
    void withOnlyUnrestrictedBlocks() {
      // Given
      Vector direction = new Vector(0, 0, 1);
      Block block1 = new Block(new BlockLocation("world", 0, 0, 0), "BEACON");
      Block block2 = new Block(new BlockLocation("world", 1, 0, 0), "BEACON");
      Block block3 = new Block(new BlockLocation("world", 0, 1, 0), "BEACON");
      Block block4 = new Block(new BlockLocation("world", 0, 0, 1), "BEACON");
      Set<Block> blocks = new HashSet<>(Arrays.asList(block1, block2, block3, block4));

      // When
      patchPlaceBreakImpl.moveTags(blocks, direction).join();

      // Then
      verify(tagRepository).updateLocations(locationPairCaptor.capture());
      OldNewBlockLocationPairSet actualValue = locationPairCaptor.getValue();

      OldNewBlockLocationPairSet expectedValue =
          new OldNewBlockLocationPairSet(
              blocks.stream()
                  .map(
                      oldBlock ->
                          new OldNewBlockLocationPair(
                              oldBlock.getBlockLocation(),
                              BlockLocation.from(oldBlock.getBlockLocation(), direction)))
                  .collect(Collectors.toSet()));

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("With only restricted block")
    void withOnlyRestrictedBlock() {
      // Given
      Vector direction = new Vector(0, 0, 1);
      Block block1 = new Block(new BlockLocation("world", 0, 0, 0), "STONE");
      Block block2 = new Block(new BlockLocation("world", 1, 0, 0), "STONE");
      Block block3 = new Block(new BlockLocation("world", 0, 1, 0), "DIRT");
      Block block4 = new Block(new BlockLocation("world", 0, 0, 1), "DIRT");
      Set<Block> blocks = new HashSet<>(Arrays.asList(block1, block2, block3, block4));

      // When
      patchPlaceBreakImpl.moveTags(blocks, direction).join();

      // Then
      verifyNoInteractions(tagRepository);
    }

    @Test
    @DisplayName("With some restricted block")
    void withSomeRestrictedBlock() {
      // Given
      Vector direction = new Vector(0, 0, 1);
      Block block1 = new Block(new BlockLocation("world", 0, 0, 0), "BEACON");
      Block block2 = new Block(new BlockLocation("world", 1, 0, 0), "DIRT");
      Block block3 = new Block(new BlockLocation("world", 0, 1, 0), "BEACON");
      Block block4 = new Block(new BlockLocation("world", 0, 0, 1), "STONE");
      Set<Block> blocks = new HashSet<>(Arrays.asList(block1, block2, block3, block4));

      // When
      patchPlaceBreakImpl.moveTags(blocks, direction).join();

      // Then
      verify(tagRepository).updateLocations(locationPairCaptor.capture());
      OldNewBlockLocationPairSet actualValue = locationPairCaptor.getValue();

      OldNewBlockLocationPairSet expectedValue =
          new OldNewBlockLocationPairSet(
              blocks.stream()
                  .filter(block -> !restrictedBlocksProperties.isRestricted(block.getMaterial()))
                  .map(
                      oldBlockLocation ->
                          new OldNewBlockLocationPair(
                              oldBlockLocation.getBlockLocation(),
                              BlockLocation.from(oldBlockLocation.getBlockLocation(), direction)))
                  .collect(Collectors.toSet()));

      assertThat(actualValue).isEqualTo(expectedValue);
    }
  }

  @Nested
  @DisplayName("Is place and break exploit")
  @TestInstance(Lifecycle.PER_CLASS)
  class IsPlaceAndBreakExploit {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With unrestricted block")
    void withUnrestrictedBlock(@Nullable Tag givenTag, boolean expectedValue) {
      // Given
      Block unrestrictedBlock = new Block(new BlockLocation("world", 0, 0, 0), "BEACON");
      given(tagRepository.findByLocation(any())).willReturn(Optional.ofNullable(givenTag));

      // When
      boolean isExploit =
          patchPlaceBreakImpl.isPlaceAndBreakExploit(BlockActionType.BREAK, unrestrictedBlock);

      // Then
      assertThat(isExploit).isEqualTo(expectedValue);
    }

    private @NotNull Stream<Arguments> withUnrestrictedBlock() {
      return Stream.of(
          arguments(
              named(
                  "With persistent tag",
                  new Tag(new BlockLocation("world", 0, 0, 0), false, LocalDateTime.now(clock))),
              true),
          arguments(named("Without tag", null), false),
          arguments(
              named(
                  "With active ephemeral tag",
                  new Tag(new BlockLocation("world", 0, 0, 0), true, LocalDateTime.now(clock))),
              true),
          arguments(
              named(
                  "With inactive ephemeral tag",
                  new Tag(
                      new BlockLocation("world", 0, 0, 0),
                      true,
                      LocalDateTime.now(clock).minusSeconds(10))),
              false));
    }

    @Test
    @DisplayName("With restricted block")
    void withRestrictedBlock() {
      // Given
      Block restrictedBlock = new Block(new BlockLocation("world", 0, 0, 0), "STONE");

      // When
      boolean isExploit =
          patchPlaceBreakImpl.isPlaceAndBreakExploit(BlockActionType.BREAK, restrictedBlock);

      // Then
      verifyNoInteractions(tagRepository);
      assertThat(isExploit).isFalse();
    }
  }
}
