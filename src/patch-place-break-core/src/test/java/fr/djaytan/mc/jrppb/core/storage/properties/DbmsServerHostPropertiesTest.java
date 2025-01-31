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

import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesAssertions.assertInstantiationFailureWithBlankHostname;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesAssertions.assertInstantiationFailureWithInvalidPort;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesAssertions.assertInstantiationFailureWithTooLongHostname;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesAssertions.assertSuccessfulInstantiation;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesAssertions.assertSuccessfulInstantiationWithHostname;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesAssertions.assertSuccessfulInstantiationWithPort;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_HOSTNAME;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_IS_SSL_ENABLED;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PORT;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.randomDbmsServerHostname;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.randomDbmsServerHostnameOfLength;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.randomDbmsServerPort;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.randomDbmsServerSslEnabled;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.randomInvalidDbmsServerPort;
import static fr.djaytan.mc.jrppb.core.storage.properties.DbmsServerHostPropertiesTestDataSet.randomTooLongDbmsServerHostname;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

final class DbmsServerHostPropertiesTest {

  @Nested
  class WhenInstantiating {

    @Nested
    class SuccessCase {

      @Test
      void withNominalValues() {
        assertSuccessfulInstantiation(
            NOMINAL_DBMS_SERVER_HOSTNAME,
            NOMINAL_DBMS_SERVER_PORT,
            NOMINAL_DBMS_SERVER_IS_SSL_ENABLED);
      }

      @Test
      void withHighestAllowedHostnameLength() {
        assertSuccessfulInstantiationWithHostname(randomDbmsServerHostnameOfLength(255));
      }

      @Test
      void withLowestAllowedHostnameLength() {
        assertSuccessfulInstantiationWithHostname(randomDbmsServerHostnameOfLength(1));
      }

      @Test
      void withHighestAllowedPort() {
        assertSuccessfulInstantiationWithPort(65535);
      }

      @Test
      void withLowestAllowedPort() {
        assertSuccessfulInstantiationWithPort(1);
      }

      @RepeatedTest(100)
      void withRandomlyGeneratedValidValues() {
        assertSuccessfulInstantiation(
            randomDbmsServerHostname(), randomDbmsServerPort(), randomDbmsServerSslEnabled());
      }
    }

    @Nested
    class FailureCase {

      @Nested
      class WithInvalidHostname {

        @Test
        void lengthJustAboveLimit() {
          assertInstantiationFailureWithTooLongHostname(randomDbmsServerHostnameOfLength(256));
        }

        @Test
        void empty() {
          assertInstantiationFailureWithBlankHostname("");
        }

        @Test
        void blank() {
          assertInstantiationFailureWithBlankHostname(" ");
        }

        @RepeatedTest(100)
        void randomlyGeneratedTooLong() {
          assertInstantiationFailureWithTooLongHostname(randomTooLongDbmsServerHostname());
        }
      }

      @Nested
      class WithInvalidPort {

        @Test
        void portZero() {
          assertInstantiationFailureWithInvalidPort(0);
        }

        @Test
        void negative() {
          assertInstantiationFailureWithInvalidPort(-1);
        }

        @Test
        void justAboveLimit() {
          assertInstantiationFailureWithInvalidPort(65536);
        }

        @RepeatedTest(100)
        void randomlyGenerated() {
          assertInstantiationFailureWithInvalidPort(randomInvalidDbmsServerPort());
        }
      }
    }
  }
}
