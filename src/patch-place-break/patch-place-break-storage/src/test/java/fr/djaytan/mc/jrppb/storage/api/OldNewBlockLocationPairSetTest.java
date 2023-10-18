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
package fr.djaytan.mc.jrppb.storage.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OldNewBlockLocationPairSetTest {

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("With nominal values")
    void withNominalValues_shouldSuccess() {
      // Given
      BlockLocation oldBlockLocation1 = new BlockLocation("world", 12, 64, -35);
      BlockLocation newBlockLocation1 = new BlockLocation("world", 12, 64, -36);
      OldNewBlockLocationPair oldNewBlockLocationPair1 =
          new OldNewBlockLocationPair(oldBlockLocation1, newBlockLocation1);

      BlockLocation oldBlockLocation2 = new BlockLocation("world", 562, 30, -55);
      BlockLocation newBlockLocation2 = new BlockLocation("world", 563, 30, -55);
      OldNewBlockLocationPair oldNewBlockLocationPair2 =
          new OldNewBlockLocationPair(oldBlockLocation2, newBlockLocation2);

      Collection<OldNewBlockLocationPair> oldNewBlockLocationPairs =
          Arrays.asList(oldNewBlockLocationPair1, oldNewBlockLocationPair2);

      // When
      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(oldNewBlockLocationPairs);

      // Then
      assertThat(oldNewBlockLocationPairSet.oldNewBlockLocationPairs())
          .containsExactlyInAnyOrderElementsOf(oldNewBlockLocationPairs);
    }

    @Nested
    @DisplayName("With mutable collection")
    class WithMutableCollection {

      @Test
      @DisplayName("And by modifying initial reference")
      void andByModifyingInitialReference_shouldRemainsImmutable() {
        // Given
        BlockLocation oldBlockLocation1 = new BlockLocation("world", 12, 64, -35);
        BlockLocation newBlockLocation1 = new BlockLocation("world", 12, 64, -36);
        OldNewBlockLocationPair oldNewBlockLocationPair1 =
            new OldNewBlockLocationPair(oldBlockLocation1, newBlockLocation1);

        Collection<OldNewBlockLocationPair> mutableOldNewBlockLocationPairs =
            new ArrayList<>(Collections.singletonList(oldNewBlockLocationPair1));

        OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
            new OldNewBlockLocationPairSet(mutableOldNewBlockLocationPairs);

        // When
        BlockLocation oldBlockLocation2 = new BlockLocation("world", 562, 30, -55);
        BlockLocation newBlockLocation2 = new BlockLocation("world", 563, 30, -55);
        OldNewBlockLocationPair oldNewBlockLocationPair2 =
            new OldNewBlockLocationPair(oldBlockLocation2, newBlockLocation2);

        mutableOldNewBlockLocationPairs.add(oldNewBlockLocationPair2);

        // Then
        assertThat(oldNewBlockLocationPairSet.oldNewBlockLocationPairs())
            .containsExactlyInAnyOrder(oldNewBlockLocationPair1);
      }

      @Test
      @DisplayName("And by modifying reference returned by getter")
      void andByModifyingReferenceReturnedByGetter_shouldRemainsImmutable() {
        // Given
        BlockLocation oldBlockLocation1 = new BlockLocation("world", 12, 64, -35);
        BlockLocation newBlockLocation1 = new BlockLocation("world", 12, 64, -36);
        OldNewBlockLocationPair oldNewBlockLocationPair1 =
            new OldNewBlockLocationPair(oldBlockLocation1, newBlockLocation1);

        Collection<OldNewBlockLocationPair> mutableOldNewBlockLocationPairs =
            new ArrayList<>(Collections.singletonList(oldNewBlockLocationPair1));

        OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
            new OldNewBlockLocationPairSet(mutableOldNewBlockLocationPairs);

        // When
        BlockLocation oldBlockLocation2 = new BlockLocation("world", 562, 30, -55);
        BlockLocation newBlockLocation2 = new BlockLocation("world", 563, 30, -55);
        OldNewBlockLocationPair oldNewBlockLocationPair2 =
            new OldNewBlockLocationPair(oldBlockLocation2, newBlockLocation2);

        Exception exception =
            catchException(
                () ->
                    oldNewBlockLocationPairSet
                        .oldNewBlockLocationPairs()
                        .add(oldNewBlockLocationPair2));

        // Then
        assertThat(exception).isExactlyInstanceOf(UnsupportedOperationException.class);
      }
    }
  }

  @Nested
  @DisplayName("When flattening block locations")
  class WhenFlatteningBlockLocations {

    @Test
    @DisplayName("With nominal values")
    void withNominalValues_shouldSuccess() {
      // Given
      BlockLocation oldBlockLocation1 = new BlockLocation("world", 12, 64, -35);
      BlockLocation newBlockLocation1 = new BlockLocation("world", 12, 64, -36);
      OldNewBlockLocationPair oldNewBlockLocationPair1 =
          new OldNewBlockLocationPair(oldBlockLocation1, newBlockLocation1);

      BlockLocation oldBlockLocation2 = new BlockLocation("world", 562, 30, -55);
      BlockLocation newBlockLocation2 = new BlockLocation("world", 563, 30, -55);
      OldNewBlockLocationPair oldNewBlockLocationPair2 =
          new OldNewBlockLocationPair(oldBlockLocation2, newBlockLocation2);

      Collection<OldNewBlockLocationPair> oldNewBlockLocationPairs =
          Arrays.asList(oldNewBlockLocationPair1, oldNewBlockLocationPair2);

      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(oldNewBlockLocationPairs);

      // When
      Set<BlockLocation> blockLocations = oldNewBlockLocationPairSet.flattenBlockLocations();

      // Then
      assertThat(blockLocations)
          .hasSize(4)
          .containsExactlyInAnyOrder(
              oldBlockLocation1, newBlockLocation1, oldBlockLocation2, newBlockLocation2);
    }

    @Test
    @DisplayName("With duplicated locations")
    void withDuplicatedLocations_shouldSuccessWithoutDuplications() {
      // Given
      BlockLocation oldBlockLocation1 = new BlockLocation("world", 12, 64, -35);
      BlockLocation newBlockLocation1 = new BlockLocation("world", 12, 64, -36);
      OldNewBlockLocationPair oldNewBlockLocationPair1 =
          new OldNewBlockLocationPair(oldBlockLocation1, newBlockLocation1);

      BlockLocation oldBlockLocation2 = new BlockLocation("world", 562, 30, -55);
      OldNewBlockLocationPair oldNewBlockLocationPair2 =
          new OldNewBlockLocationPair(oldBlockLocation2, newBlockLocation1);

      Collection<OldNewBlockLocationPair> oldNewBlockLocationPairs =
          Arrays.asList(oldNewBlockLocationPair1, oldNewBlockLocationPair2);

      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(oldNewBlockLocationPairs);

      // When
      Set<BlockLocation> blockLocations = oldNewBlockLocationPairSet.flattenBlockLocations();

      // Then
      assertThat(blockLocations)
          .hasSize(3)
          .containsExactlyInAnyOrder(oldBlockLocation1, newBlockLocation1, oldBlockLocation2);
    }

    @Test
    @DisplayName("Without locations")
    void withoutLocations_shouldSuccessWithEmptySet() {
      // Given
      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(Collections.emptyList());

      // When
      Set<BlockLocation> blockLocations = oldNewBlockLocationPairSet.flattenBlockLocations();

      // Then
      assertThat(blockLocations).isEmpty();
    }
  }
}
