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
package fr.djaytan.mc.jrppb.paper.adapter.converter;

import static fr.djaytan.mc.jrppb.paper.adapter.JobActionTypeSupportChecker.getSupportedJobActionTypes;
import static fr.djaytan.mc.jrppb.paper.adapter.JobActionTypeSupportChecker.isSupportedJobActionType;

import com.gamingmesh.jobs.container.ActionType;
import fr.djaytan.mc.jrppb.api.entities.BlockActionType;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ActionTypeConverter implements UnidirectionalConverter<ActionType, BlockActionType> {

  @Override
  public @NotNull BlockActionType convert(@NotNull ActionType jobActionType) {
    Validate.isTrue(
        isSupportedJobActionType(jobActionType),
        "Unsupported job action type '%s' specified. Only the following ones are supported: %s",
        jobActionType,
        getSupportedJobActionTypes());

    return BlockActionType.valueOf(jobActionType.name());
  }
}
