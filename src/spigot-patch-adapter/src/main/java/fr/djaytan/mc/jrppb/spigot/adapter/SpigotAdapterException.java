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
package fr.djaytan.mc.jrppb.spigot.adapter;

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.mc.jrppb.api.entities.BlockActionType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class SpigotAdapterException extends RuntimeException {

  private static final String INVALID_JOB_TYPE =
      "Invalid job action type '%s' specified. Expecting one of the following: %s";

  private SpigotAdapterException(@NotNull String message) {
    super(message);
  }

  public static @NotNull SpigotAdapterException invalidJobType(
      @NotNull ActionType invalidJobActionType) {
    String validPatchActionTypes = StringUtils.join(BlockActionType.values(), ", ");
    String message = String.format(INVALID_JOB_TYPE, invalidJobActionType, validPatchActionTypes);
    return new SpigotAdapterException(message);
  }
}