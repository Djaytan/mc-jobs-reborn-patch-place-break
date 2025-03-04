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
package fr.djaytan.mc.jrppb.core;

import static java.util.Collections.singleton;

import java.util.Set;
import java.util.TreeSet;

public final class RestrictedBlocksPropertiesTestDataSet {

  public static final RestrictionMode NOMINAL_RESTRICTION_MODE = RestrictionMode.BLACKLIST;

  public static final String NOMINAL_LISTED_MATERIAL = "STONE";
  public static final String NOMINAL_UNLISTED_MATERIAL = "COBBLESTONE";

  public static final Set<String> NOMINAL_SINGLETON_RESTRICTION_LIST =
      singleton(NOMINAL_LISTED_MATERIAL);
  public static final Set<String> NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST =
      new TreeSet<>(Set.of(NOMINAL_LISTED_MATERIAL, "DIRT", "SAND"));

  public static final RestrictedBlocksProperties NOMINAL_RESTRICTED_BLOCKS_PROPERTIES =
      new RestrictedBlocksProperties(
          NOMINAL_RESTRICTION_MODE, NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST);
}
