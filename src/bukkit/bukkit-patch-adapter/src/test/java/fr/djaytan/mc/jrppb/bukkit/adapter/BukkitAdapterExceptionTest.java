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
package fr.djaytan.mc.jrppb.bukkit.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.gamingmesh.jobs.container.ActionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BukkitAdapterExceptionTest {

  @Test
  @DisplayName("When instantiating invalid job type exception")
  void whenInstantiatingInvalidJobTypeException() {
    // Given
    ActionType invalidActionType = ActionType.CRAFT;

    // When
    BukkitAdapterException bukkitAdapterException =
        BukkitAdapterException.invalidJobType(invalidActionType);

    // Then
    assertThat(bukkitAdapterException)
        .hasMessage(
            "Invalid job action type 'CRAFT' specified. Expecting one of the following: BREAK, TNTBREAK, PLACE");
  }
}
