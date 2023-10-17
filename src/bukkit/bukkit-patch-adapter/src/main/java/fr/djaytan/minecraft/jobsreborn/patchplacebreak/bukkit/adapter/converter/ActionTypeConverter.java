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

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockActionType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.BukkitAdapterException;
import jakarta.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ActionTypeConverter implements UnidirectionalConverter<ActionType, BlockActionType> {

  @Override
  public @NotNull BlockActionType convert(@NotNull ActionType jobActionType) {
    if (!isValidJobActionType(jobActionType)) {
      throw BukkitAdapterException.invalidJobType(jobActionType);
    }
    return BlockActionType.valueOf(jobActionType.name());
  }

  private boolean isValidJobActionType(@NotNull ActionType jobActionType) {
    Collection<ActionType> validJobActionTypes = getValidJobActionTypes();
    return validJobActionTypes.contains(jobActionType);
  }

  private @NotNull Collection<ActionType> getValidJobActionTypes() {
    return Arrays.stream(BlockActionType.values())
        .map(BlockActionType::name)
        .map(ActionType::valueOf)
        .collect(Collectors.toList());
  }
}
