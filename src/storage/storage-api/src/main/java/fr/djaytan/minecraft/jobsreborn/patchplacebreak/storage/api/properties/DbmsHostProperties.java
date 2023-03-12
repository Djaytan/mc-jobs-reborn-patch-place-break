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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties;

import lombok.NonNull;

/** Represents the DBMS server properties related to the host. */
public interface DbmsHostProperties {

  /**
   * Gets the hostname of the targeted DBMS server.
   *
   * @return The hostname of the targeted DBMS server.
   */
  @NonNull
  String getHostname();

  /**
   * Gets the port on which the targeted DBMS server is exposed.
   *
   * @return The port on which the targeted DBMS server is exposed.
   */
  int getPort();

  /**
   * Gets <code>true</code> if the SSL communication is enabled, <code>false</code> otherwise.
   *
   * @return <code>true</code> if the SSL communication is enabled, <code>false</code> otherwise.
   */
  boolean isSslEnabled();
}
