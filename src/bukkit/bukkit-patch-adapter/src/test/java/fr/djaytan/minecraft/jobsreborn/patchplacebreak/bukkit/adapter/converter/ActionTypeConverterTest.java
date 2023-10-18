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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.BukkitAdapterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ActionTypeConverterTest {

  private final ActionTypeConverter actionTypeConverter = new ActionTypeConverter();

  @Test
  @DisplayName("When converting with valid action type")
  void whenConverting_withValidActionType_shouldReturnBlockActionType() {
    // Given
    ActionType validActionType = ActionType.BREAK;

    // When
    BlockActionType blockActionType = actionTypeConverter.convert(validActionType);

    // Then
    assertThat(blockActionType).isEqualTo(BlockActionType.BREAK);
  }

  @Test
  @DisplayName("When converting with invalid action type")
  void whenConverting_withInvalidActionType_shouldThrowException() {
    // Given
    ActionType invalidActionType = ActionType.BREED;

    // When
    Exception exception = catchException(() -> actionTypeConverter.convert(invalidActionType));

    // Then
    assertThat(exception)
        .isExactlyInstanceOf(BukkitAdapterException.class)
        .hasMessage(
            "Invalid job action type 'BREED' specified. Expecting one of the following: BREAK, TNTBREAK, PLACE");
  }
}
