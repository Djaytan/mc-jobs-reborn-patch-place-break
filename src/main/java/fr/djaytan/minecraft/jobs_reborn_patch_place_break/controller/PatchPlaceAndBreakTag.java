/*
 * JobsReborn extension to patch place-break (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.djaytan.minecraft.jobs_reborn_patch_place_break.controller;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.time.LocalDateTime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatchPlaceAndBreakTag {

  private LocalDateTime initLocalDateTime;
  private Duration duration;

  public PatchPlaceAndBreakTag(
      @NotNull LocalDateTime initLocalDateTime, @Nullable Duration duration) {
    setInitLocalDateTime(initLocalDateTime);
    setDuration(duration);
  }

  public LocalDateTime getInitLocalDateTime() {
    return initLocalDateTime;
  }

  public Duration getDuration() {
    return duration;
  }

  private void setInitLocalDateTime(@NotNull LocalDateTime initLocalDateTime) {
    Preconditions.checkNotNull(initLocalDateTime);
    this.initLocalDateTime = initLocalDateTime;
  }

  private void setDuration(@Nullable Duration duration) {
    this.duration = duration;
  }
}
