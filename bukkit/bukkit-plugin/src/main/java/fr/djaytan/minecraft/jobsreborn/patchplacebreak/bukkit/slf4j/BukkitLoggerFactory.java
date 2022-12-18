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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.slf4j;

import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.JobsRebornPatchPlaceBreakPlugin;

/**
 * /!\ WARNING /!\
 * <p>
 *  This class is a copy from the slf4j-jdk14 module:
 *  <a href="https://github.com/qos-ch/slf4j/edit/master/slf4j-jdk14/src/main/java/org/slf4j/jul/JDK14LoggerFactory.java">source</a>
 * </p>
 * <p>
 *  The purpose is to permit the creation of a SLF4J instance of the Bukkit logger to ease the decoupling.
 * </p>
 */
public class BukkitLoggerFactory implements ILoggerFactory {

  @Override
  public Logger getLogger(String name) {
    return new BukkitLoggerAdapter(
        JavaPlugin.getPlugin(JobsRebornPatchPlaceBreakPlugin.class).getLogger());
  }
}
