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
package fr.djaytan.mc.jrppb.core.storage.properties;

import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOSTNAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_IS_SSL_ENABLED;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jetbrains.annotations.NotNull;

public final class DbmsServerHostPropertiesAssertions {

  public static void assertSuccessfulInstantiation(
      @NotNull String hostname, int port, boolean isSslEnabled) {
    assertThat(new DbmsServerHostProperties(hostname, port, isSslEnabled))
        .satisfies(v -> assertThat(v.hostname()).isEqualTo(hostname))
        .satisfies(v -> assertThat(v.port()).isEqualTo(port))
        .satisfies(v -> assertThat(v.isSslEnabled()).isEqualTo(isSslEnabled));
  }

  public static void assertSuccessfulInstantiationWithHostname(@NotNull String hostname) {
    assertThatCode(
            () ->
                new DbmsServerHostProperties(
                    hostname, NOMINAL_DBMS_SERVER_PORT, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .doesNotThrowAnyException();
  }

  public static void assertSuccessfulInstantiationWithPort(int port) {
    assertThatCode(
            () ->
                new DbmsServerHostProperties(
                    NOMINAL_DBMS_SERVER_HOSTNAME, port, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .doesNotThrowAnyException();
  }

  public static void assertInstantiationFailureWithTooLongHostname(@NotNull String hostname) {
    assertThatThrownBy(
            () ->
                new DbmsServerHostProperties(
                    hostname, NOMINAL_DBMS_SERVER_PORT, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The DBMS server hostname cannot exceed 255 characters");
  }

  public static void assertInstantiationFailureWithBlankHostname(@NotNull String hostname) {
    assertThatThrownBy(
            () ->
                new DbmsServerHostProperties(
                    hostname, NOMINAL_DBMS_SERVER_PORT, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The DBMS server hostname cannot be blank");
  }

  public static void assertInstantiationFailureWithInvalidPort(int port) {
    assertThatThrownBy(
            () ->
                new DbmsServerHostProperties(
                    NOMINAL_DBMS_SERVER_HOSTNAME, port, NOMINAL_DBMS_SERVER_IS_SSL_ENABLED))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The DBMS server port must be between 1 and 65535");
  }
}
