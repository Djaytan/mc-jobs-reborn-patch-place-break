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

import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_LISTED_MATERIAL;
import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST;
import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_RESTRICTION_MODE;
import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_SINGLETON_RESTRICTION_LIST;
import static fr.djaytan.mc.jrppb.core.RestrictedBlocksPropertiesTestDataSet.NOMINAL_UNLISTED_MATERIAL;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class RestrictedBlocksPropertiesTest {

  @Nested
  class WhenInstantiating {

    @Test
    void nominalCase() {
      assertThat(
              new RestrictedBlocksProperties(
                  NOMINAL_RESTRICTION_MODE, NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST))
          .satisfies(
              v -> assertThat(v.materials()).isEqualTo(NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST))
          .satisfies(v -> assertThat(v.restrictionMode()).isEqualTo(NOMINAL_RESTRICTION_MODE));
    }

    /**
     * It is totally fine when experimenting and analyzing setups to spot bottlenecks. Stated
     * differently: it would be annoying to be forced to empty the restriction list anytime we need
     * to disable it.
     */
    @Test
    void withDisabledRestrictionMode_andNonEmptyRestrictionList_thenShallSucceed() {
      assertThatCode(
              () ->
                  new RestrictedBlocksProperties(
                      RestrictionMode.DISABLED, NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST))
          .doesNotThrowAnyException();
    }
  }

  @Nested
  class WhenCheckingMaterialRestriction {

    @Nested
    class WithDisabledRestrictionMode {

      @Test
      void whenCheckedMaterialIsListed() {
        assertUnrestrictedMaterialWhenDisabledRestriction(NOMINAL_LISTED_MATERIAL);
      }

      @Test
      void whenCheckedMaterialIsUnlisted() {
        assertUnrestrictedMaterialWhenDisabledRestriction(NOMINAL_UNLISTED_MATERIAL);
      }

      private static void assertUnrestrictedMaterialWhenDisabledRestriction(
          @NotNull String material) {
        assertThat(
                new RestrictedBlocksProperties(
                        RestrictionMode.DISABLED, NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST)
                    .isRestricted(material))
            .isFalse();
      }
    }

    @Nested
    class WithBlacklistRestrictionMode {

      @Nested
      class ThenShallBeRestricted_WhenCheckedMaterialIsListed {

        @Test
        void singletonRestrictionList() {
          assertRestrictedMaterialWhenBlacklisted(NOMINAL_SINGLETON_RESTRICTION_LIST);
        }

        @Test
        void multiElementsRestrictionList() {
          assertRestrictedMaterialWhenBlacklisted(NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST);
        }

        private static void assertRestrictedMaterialWhenBlacklisted(
            @NotNull Set<String> blacklist) {
          assertThat(
                  new RestrictedBlocksProperties(RestrictionMode.BLACKLIST, blacklist)
                      .isRestricted(NOMINAL_LISTED_MATERIAL))
              .isTrue();
        }
      }

      @Nested
      class ThenShallBeUnrestricted_WhenCheckedMaterialIsUnlisted {

        @Test
        void emptyRestrictionList() {
          assertUnrestrictedMaterialWhenNotBlacklisted(emptySet());
        }

        @Test
        void singletonRestrictionList() {
          assertUnrestrictedMaterialWhenNotBlacklisted(NOMINAL_SINGLETON_RESTRICTION_LIST);
        }

        @Test
        void multiElementsRestrictionList() {
          assertUnrestrictedMaterialWhenNotBlacklisted(NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST);
        }

        private static void assertUnrestrictedMaterialWhenNotBlacklisted(
            @NotNull Set<String> blacklist) {
          assertThat(
                  new RestrictedBlocksProperties(RestrictionMode.BLACKLIST, blacklist)
                      .isRestricted(NOMINAL_UNLISTED_MATERIAL))
              .isFalse();
        }
      }
    }

    @Nested
    class WithWhitelistRestrictionMode {

      @Nested
      class ThenShallBeUnrestricted_WhenCheckedMaterialIsListed {

        @Test
        void singletonRestrictionList() {
          assertUnrestrictedMaterialWhenWhitelisted(NOMINAL_SINGLETON_RESTRICTION_LIST);
        }

        @Test
        void multiElementsRestrictionList() {
          assertUnrestrictedMaterialWhenWhitelisted(NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST);
        }

        private static void assertUnrestrictedMaterialWhenWhitelisted(
            @NotNull Set<String> whitelist) {
          assertThat(
                  new RestrictedBlocksProperties(RestrictionMode.WHITELIST, whitelist)
                      .isRestricted(NOMINAL_LISTED_MATERIAL))
              .isFalse();
        }
      }

      @Nested
      class ThenShallBeRestricted_WhenCheckedMaterialIsUnlisted {

        @Test
        void emptyRestrictionList() {
          assertRestrictedMaterialWhenNotWhitelisted(emptySet());
        }

        @Test
        void singletonRestrictionList() {
          assertRestrictedMaterialWhenNotWhitelisted(NOMINAL_SINGLETON_RESTRICTION_LIST);
        }

        @Test
        void multiElementsRestrictionList() {
          assertRestrictedMaterialWhenNotWhitelisted(NOMINAL_MULTI_ELEMENTS_RESTRICTION_LIST);
        }

        private static void assertRestrictedMaterialWhenNotWhitelisted(
            @NotNull Set<String> whitelist) {
          assertThat(
                  new RestrictedBlocksProperties(RestrictionMode.WHITELIST, whitelist)
                      .isRestricted(NOMINAL_UNLISTED_MATERIAL))
              .isTrue();
        }
      }
    }
  }
}
