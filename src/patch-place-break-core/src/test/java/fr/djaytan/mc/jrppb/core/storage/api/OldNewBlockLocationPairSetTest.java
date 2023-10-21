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
package fr.djaytan.mc.jrppb.core.storage.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import fr.djaytan.mc.jrppb.api.entities.BlockLocation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OldNewBlockLocationPairSetTest {

  private static final BlockLocation OLD_BLOCK_LOCATION_1 = new BlockLocation("world", 12, 64, -35);
  private static final BlockLocation NEW_BLOCK_LOCATION_1 = new BlockLocation("world", 12, 64, -36);
  private static final OldNewBlockLocationPair OLD_NEW_BLOCK_LOCATION_PAIR_1 =
      new OldNewBlockLocationPair(OLD_BLOCK_LOCATION_1, NEW_BLOCK_LOCATION_1);

  private static final BlockLocation OLD_BLOCK_LOCATION_2 = new BlockLocation("world", 52, 30, -55);
  private static final BlockLocation NEW_BLOCK_LOCATION_2 = new BlockLocation("world", 53, 30, -55);
  private static final OldNewBlockLocationPair OLD_NEW_BLOCK_LOCATION_PAIR_2 =
      new OldNewBlockLocationPair(OLD_BLOCK_LOCATION_2, NEW_BLOCK_LOCATION_2);

  private static final Collection<OldNewBlockLocationPair> OLD_NEW_BLOCK_LOCATION_PAIR_COLLECTION =
      Arrays.asList(OLD_NEW_BLOCK_LOCATION_PAIR_1, OLD_NEW_BLOCK_LOCATION_PAIR_2);

  @Nested
  class WhenInstantiating {

    @Test
    void withNominalValues_shouldSuccess() {
      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(OLD_NEW_BLOCK_LOCATION_PAIR_COLLECTION);

      assertThat(oldNewBlockLocationPairSet.oldNewBlockLocationPairs())
          .containsExactlyInAnyOrderElementsOf(OLD_NEW_BLOCK_LOCATION_PAIR_COLLECTION);
    }

    @Nested
    class WithMutableCollection {

      @Test
      void andByModifyingInitialReference_shouldRemainsImmutable() {
        // Given
        Collection<OldNewBlockLocationPair> mutableOldNewBlockLocationPairs =
            new ArrayList<>(Collections.singletonList(OLD_NEW_BLOCK_LOCATION_PAIR_1));
        OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
            new OldNewBlockLocationPairSet(mutableOldNewBlockLocationPairs);

        // When
        mutableOldNewBlockLocationPairs.add(OLD_NEW_BLOCK_LOCATION_PAIR_2);

        // Then
        assertThat(oldNewBlockLocationPairSet.oldNewBlockLocationPairs())
            .containsExactly(OLD_NEW_BLOCK_LOCATION_PAIR_1);
      }

      @Test
      void andByModifyingReferenceReturnedByGetter_shouldRemainsImmutable() {
        // Given
        Collection<OldNewBlockLocationPair> mutableOldNewBlockLocationPairs =
            new ArrayList<>(Collections.singletonList(OLD_NEW_BLOCK_LOCATION_PAIR_1));
        OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
            new OldNewBlockLocationPairSet(mutableOldNewBlockLocationPairs);

        // When
        Exception exception =
            catchException(
                () ->
                    oldNewBlockLocationPairSet
                        .oldNewBlockLocationPairs()
                        .add(OLD_NEW_BLOCK_LOCATION_PAIR_2));

        // Then
        assertThat(exception).isExactlyInstanceOf(UnsupportedOperationException.class);
      }
    }
  }

  @Nested
  class WhenFlatteningBlockLocations {

    @Test
    void withNominalValues_shouldSuccess() {
      // Given
      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(OLD_NEW_BLOCK_LOCATION_PAIR_COLLECTION);

      // When
      Set<BlockLocation> blockLocations = oldNewBlockLocationPairSet.flattenBlockLocations();

      // Then
      assertThat(blockLocations)
          .hasSize(4)
          .containsExactlyInAnyOrder(
              OLD_BLOCK_LOCATION_1,
              NEW_BLOCK_LOCATION_1,
              OLD_BLOCK_LOCATION_2,
              NEW_BLOCK_LOCATION_2);
    }

    @Test
    void withDuplicatedLocations_shouldSuccessWithoutDuplications() {
      // Given
      OldNewBlockLocationPair oldNewBlockLocationPairWithDuplication =
          new OldNewBlockLocationPair(OLD_BLOCK_LOCATION_2, NEW_BLOCK_LOCATION_1);
      OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
          new OldNewBlockLocationPairSet(
              Arrays.asList(OLD_NEW_BLOCK_LOCATION_PAIR_1, oldNewBlockLocationPairWithDuplication));

      // When
      Set<BlockLocation> blockLocations = oldNewBlockLocationPairSet.flattenBlockLocations();

      // Then
      assertThat(blockLocations)
          .hasSize(3)
          .containsExactlyInAnyOrder(
              OLD_BLOCK_LOCATION_1, NEW_BLOCK_LOCATION_1, OLD_BLOCK_LOCATION_2);
    }

    @Test
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
