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

import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesAssertions.assertInstantiationFailureWithInvalidConnectionTimeout;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesAssertions.assertInstantiationFailureWithInvalidPoolSize;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesAssertions.assertSuccessfulInstantiation;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesAssertions.assertSuccessfulInstantiationWithConnectionPool;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesAssertions.assertSuccessfulInstantiationWithPoolSize;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_CONNECTION_TIMEOUT;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.NOMINAL_POOL_SIZE;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.randomConnectionTimeout;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.randomInvalidConnectionTimeout;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.randomInvalidPoolSize;
import static fr.djaytan.mc.jrppb.core.storage.properties.ConnectionPoolPropertiesTestDataSet.randomPoolSize;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

final class ConnectionPoolPropertiesTest {

  @Nested
  class WhenInstantiating {

    @Nested
    class SuccessCase {

      @Test
      void withNominalValues() {
        assertSuccessfulInstantiation(NOMINAL_CONNECTION_TIMEOUT, NOMINAL_POOL_SIZE);
      }

      @Test
      void withHighestAllowedConnectionTimeout() {
        assertSuccessfulInstantiationWithConnectionPool(600000);
      }

      @Test
      void withLowestAllowedConnectionTimeout() {
        assertSuccessfulInstantiationWithConnectionPool(1);
      }

      @Test
      void withHighestAllowedPoolSize() {
        assertSuccessfulInstantiationWithPoolSize(100);
      }

      @Test
      void withLowestAllowedPoolSize() {
        assertSuccessfulInstantiationWithPoolSize(1);
      }

      @RepeatedTest(100)
      void withRandomlyGeneratedValidValues() {
        assertSuccessfulInstantiation(randomConnectionTimeout(), randomPoolSize());
      }
    }

    @Nested
    class FailureCase {

      @Nested
      class WithInvalidConnectionPool {

        @Test
        void justAboveLimit() {
          assertInstantiationFailureWithInvalidConnectionTimeout(600001);
        }

        @Test
        void justBelowLimit() {
          assertInstantiationFailureWithInvalidConnectionTimeout(0);
        }

        @Test
        void negative() {
          assertInstantiationFailureWithInvalidConnectionTimeout(-1);
        }

        @RepeatedTest(100)
        void randomlyGenerated() {
          assertInstantiationFailureWithInvalidConnectionTimeout(randomInvalidConnectionTimeout());
        }
      }

      @Nested
      class WithInvalidPoolSize {

        @Test
        void justAboveLimit() {
          assertInstantiationFailureWithInvalidPoolSize(101);
        }

        @Test
        void justBelowLimit() {
          assertInstantiationFailureWithInvalidPoolSize(0);
        }

        @Test
        void negative() {
          assertInstantiationFailureWithInvalidPoolSize(-1);
        }

        @RepeatedTest(100)
        void randomlyGenerated() {
          assertInstantiationFailureWithInvalidPoolSize(randomInvalidPoolSize());
        }
      }
    }
  }
}
