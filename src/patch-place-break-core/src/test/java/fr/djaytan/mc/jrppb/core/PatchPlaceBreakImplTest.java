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
package fr.djaytan.mc.jrppb.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import fr.djaytan.mc.jrppb.api.entities.Block;
import fr.djaytan.mc.jrppb.api.entities.BlockActionType;
import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import fr.djaytan.mc.jrppb.api.entities.Tag;
import fr.djaytan.mc.jrppb.api.entities.Vector;
import fr.djaytan.mc.jrppb.api.properties.RestrictedBlocksProperties;
import fr.djaytan.mc.jrppb.api.properties.RestrictionMode;
import fr.djaytan.mc.jrppb.core.config.properties.RestrictedBlocksPropertiesImpl;
import fr.djaytan.mc.jrppb.core.storage.api.OldNewBlockLocationPair;
import fr.djaytan.mc.jrppb.core.storage.api.OldNewBlockLocationPairSet;
import fr.djaytan.mc.jrppb.core.storage.api.TagRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

  private static final Clock CLOCK = Clock.fixed(Instant.now(), ZoneId.systemDefault());

  @Mock private TagRepository tagRepository;
  @Captor private ArgumentCaptor<Tag> tagCaptor;
  @Captor private ArgumentCaptor<OldNewBlockLocationPairSet> locationPairCaptor;
  @Captor private ArgumentCaptor<BlockLocation> blockLocationCaptor;
  private final RestrictedBlocksProperties restrictedBlocksProperties =
      new RestrictedBlocksPropertiesImpl(
          new HashSet<>(Arrays.asList("STONE", "DIRT")), RestrictionMode.BLACKLIST);
  private PatchPlaceBreakImpl patchPlaceBreakImpl;

  @BeforeEach
  void beforeEach() {
    BlocksFilter blocksFilter = new BlocksFilter(restrictedBlocksProperties);

    this.patchPlaceBreakImpl =
        new PatchPlaceBreakImpl(blocksFilter, CLOCK, restrictedBlocksProperties, tagRepository);
  }

  @Nested
  class WhenPuttingTag {

    @Test
    void withNotRestrictedBlock_shouldPutExpectedPersistentTag() {
      // Given
      BlockLocation blockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToTag = new Block(blockLocation, "BEACON");

      // When
      patchPlaceBreakImpl.putTag(blockToTag, false).join();

      // Then
      verify(tagRepository).put(tagCaptor.capture());
      Tag tag = tagCaptor.getValue();

      assertAll(
          () -> assertThat(tag.blockLocation()).isEqualTo(blockLocation),
          () -> assertThat(tag.isEphemeral()).isFalse());
    }

    @Test
    void withRestrictedBlock_shouldNotAttemptToPutAnyTag() {
      // Given
      BlockLocation blockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToTag = new Block(blockLocation, "STONE");

      // When
      patchPlaceBreakImpl.putTag(blockToTag, false).join();

      // Then
      verifyNoInteractions(tagRepository);
    }

    @Test
    void withEphemeralTag_shouldPutExpectedEphemeralTag() {
      // Given
      BlockLocation blockLocation = new BlockLocation("world", 0, 0, 0);
      Block blockToTag = new Block(blockLocation, "BEACON");

      // When
      patchPlaceBreakImpl.putTag(blockToTag, true).join();

      // Then
      verify(tagRepository).put(tagCaptor.capture());
      Tag tag = tagCaptor.getValue();

      assertAll(
          () -> assertThat(tag.blockLocation()).isEqualTo(blockLocation),
          () -> assertThat(tag.isEphemeral()).isTrue());
    }
  }

  @Nested
  class WhenRemovingTag {

    @Test
    void fromNotRestrictedBlock_shouldRemoveTag() {
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
    void fromRestrictedBlock_shouldNotAttemptToRemoveAnyTag() {
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
  class WhenMovingTags {

    @Test
    void withOnlyUnrestrictedBlocks_shouldAttemptToMoveTagsAsExpected() {
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
                              oldBlock.blockLocation(),
                              BlockLocation.from(oldBlock.blockLocation(), direction)))
                  .collect(Collectors.toSet()));

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void withOnlyRestrictedBlock_shouldNotTryToMoveAnyTag() {
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
    void withSomeRestrictedBlock_shouldTryToMoveOnlyTagsFromUnrestrictedBlocks() {
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
                  .filter(block -> !restrictedBlocksProperties.isRestricted(block.material()))
                  .map(
                      oldBlockLocation ->
                          new OldNewBlockLocationPair(
                              oldBlockLocation.blockLocation(),
                              BlockLocation.from(oldBlockLocation.blockLocation(), direction)))
                  .collect(Collectors.toSet()));

      assertThat(actualValue).isEqualTo(expectedValue);
    }
  }

  @Nested
  class WhenCheckingPlaceAndBreakExploit {

    @ParameterizedTest
    @MethodSource
    void whileBreakingUnrestrictedBlock(@Nullable Tag givenTag, boolean expectedValue) {
      // Given
      Block unrestrictedBlock = new Block(new BlockLocation("world", 0, 0, 0), "BEACON");
      given(tagRepository.findByLocation(any())).willReturn(Optional.ofNullable(givenTag));

      // When
      boolean isExploit =
          patchPlaceBreakImpl.isPlaceAndBreakExploit(BlockActionType.BREAK, unrestrictedBlock);

      // Then
      assertThat(isExploit).isEqualTo(expectedValue);
    }

    private static @NotNull Stream<Arguments> whileBreakingUnrestrictedBlock() {
      return Stream.of(
          arguments(
              named(
                  "With persistent tag",
                  new Tag(new BlockLocation("world", 0, 0, 0), false, LocalDateTime.now(CLOCK))),
              named("Should be detected as exploit", true)),
          arguments(named("Without tag", null), false),
          arguments(
              named(
                  "With active ephemeral tag",
                  new Tag(new BlockLocation("world", 0, 0, 0), true, LocalDateTime.now(CLOCK))),
              named("Should be detected as exploit", true)),
          arguments(
              named(
                  "With inactive ephemeral tag",
                  new Tag(
                      new BlockLocation("world", 0, 0, 0),
                      true,
                      LocalDateTime.now(CLOCK).minusSeconds(10))),
              named("Should NOT be detected as exploit", false)));
    }

    @Test
    void whileBreakingRestrictedBlock_shouldNotConsiderActionAsExploitNorPerformDeepCheck() {
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
