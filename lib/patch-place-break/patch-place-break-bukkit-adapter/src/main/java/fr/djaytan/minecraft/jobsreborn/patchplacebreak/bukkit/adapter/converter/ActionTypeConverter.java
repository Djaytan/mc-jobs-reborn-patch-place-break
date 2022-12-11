/*
 * MIT License
 *
 * Copyright (c) 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.gamingmesh.jobs.container.ActionType;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.PatchActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.BukkitAdapterException;
import lombok.NonNull;

@Singleton
public class ActionTypeConverter implements UnidirectionalConverter<ActionType, PatchActionType> {

  @Override
  public @NonNull PatchActionType convert(@NonNull ActionType jobActionType) {
    if (!isValidJobActionType(jobActionType)) {
      throw BukkitAdapterException.invalidJobType(jobActionType);
    }
    return PatchActionType.valueOf(jobActionType.name());
  }

  private boolean isValidJobActionType(@NonNull ActionType jobActionType) {
    List<ActionType> validJobActionTypes = getValidJobActionTypes();
    return validJobActionTypes.contains(jobActionType);
  }

  private @NonNull List<ActionType> getValidJobActionTypes() {
    return Arrays.stream(PatchActionType.values()).map(PatchActionType::name)
        .map(ActionType::valueOf).collect(Collectors.toList());
  }
}
