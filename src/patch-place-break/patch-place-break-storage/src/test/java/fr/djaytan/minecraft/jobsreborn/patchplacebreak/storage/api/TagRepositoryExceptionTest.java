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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api;

import static org.assertj.core.api.Assertions.assertThat;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagRepositoryExceptionTest {

  @Test
  @DisplayName("When instantiating put tag exception")
  void whenInstantiatingPutTagException() {
    // Given
    Tag tag = new Tag(new BlockLocation("world", 123, 54, -762), false, LocalDateTime.now());
    Throwable cause = new IOException();

    // When
    TagRepositoryException tagRepositoryException = TagRepositoryException.put(tag, cause);

    // Then
    assertThat(tagRepositoryException)
        .hasCause(cause)
        .hasMessageMatching("Failed to put the following tag: .*")
        .hasMessageContaining(tag.toString());
  }

  @Test
  @DisplayName("When instantiating update tag exception")
  void whenInstantiatingUpdateTagException() {
    // Given
    BlockLocation oldBlockLocation = new BlockLocation("world", 123, 54, -762);
    BlockLocation newBlockLocation = new BlockLocation("world", 124, 54, -762);
    OldNewBlockLocationPair oldNewBlockLocationPair =
        new OldNewBlockLocationPair(oldBlockLocation, newBlockLocation);
    OldNewBlockLocationPairSet oldNewBlockLocationPairSet =
        new OldNewBlockLocationPairSet(Collections.singleton(oldNewBlockLocationPair));

    Throwable cause = new IOException();

    // When
    TagRepositoryException tagRepositoryException =
        TagRepositoryException.update(oldNewBlockLocationPairSet, cause);

    // Then
    assertThat(tagRepositoryException)
        .hasCause(cause)
        .hasMessageMatching(
            "Failed to update the tags for the following old-new location pairs: .*")
        .hasMessageContaining(oldNewBlockLocationPairSet.toString());
  }

  @Test
  @DisplayName("When instantiating fetch tag exception")
  void whenInstantiatingFetchTagException() {
    // Given
    BlockLocation blockLocation = new BlockLocation("world", 123, 54, -762);
    Throwable cause = new IOException();

    // When
    TagRepositoryException tagRepositoryException =
        TagRepositoryException.fetch(blockLocation, cause);

    // Then
    assertThat(tagRepositoryException)
        .hasCause(cause)
        .hasMessageMatching("Failed to fetch the tag with the following location: .*")
        .hasMessageContaining(blockLocation.toString());
  }

  @Test
  @DisplayName("When instantiating delete tag exception")
  void whenInstantiatingDeleteTagException() {
    // Given
    BlockLocation blockLocation = new BlockLocation("world", 123, 54, -762);
    Throwable cause = new IOException();

    // When
    TagRepositoryException tagRepositoryException =
        TagRepositoryException.delete(blockLocation, cause);

    // Then
    assertThat(tagRepositoryException)
        .hasCause(cause)
        .hasMessageMatching("Failed to delete the tag with the following location: .*")
        .hasMessageContaining(blockLocation.toString());
  }
}
