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
package fr.djaytan.mc.jrppb.api.entities;

import fr.djaytan.mc.jrppb.api.PatchPlaceBreakApi;
import java.time.LocalDateTime;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a place-and-break tag for the patch. The purpose of this tag is to be
 * attached to the metadata of a block.
 *
 * <p>When an instance of this class is found in the metadata of a given block, this means that the
 * patch must be applied for eligible {@link BlockActionType}. See {@link
 * PatchPlaceBreakApi#isPlaceAndBreakExploit(BlockActionType, Block)} for more details about it.
 *
 * <p>This tag contains the following information:
 *
 * <ul>
 *   <li>The initial date-time of when the tag as been created ;
 *   <li>The validity duration of the tag. If null, this means that the tag isn't an "ephemeral" one
 *       and will be persisted forever until removed explicitly from metadata of block (e.g. by
 *       calling {@link PatchPlaceBreakApi#removeTag(Block)}).
 * </ul>
 *
 * <p>This class is thread-safe and immutable.
 *
 * @param blockLocation The block's location.
 * @param isEphemeral Whether the tag is ephemeral or not.
 * @param createdAt The local-date time since which the tag is created.
 */
public record Tag(
    @NotNull BlockLocation blockLocation, boolean isEphemeral, @NotNull LocalDateTime createdAt) {}
