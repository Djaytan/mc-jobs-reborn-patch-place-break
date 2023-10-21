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
package fr.djaytan.mc.jrppb.core.config.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;

import fr.djaytan.mc.jrppb.core.config.properties.ConnectionPoolPropertiesImpl;
import fr.djaytan.mc.jrppb.core.config.properties.DataSourcePropertiesImpl;
import fr.djaytan.mc.jrppb.core.config.properties.DbmsCredentialsPropertiesImpl;
import fr.djaytan.mc.jrppb.core.config.properties.DbmsHostPropertiesImpl;
import fr.djaytan.mc.jrppb.core.config.properties.DbmsServerPropertiesImpl;
import fr.djaytan.mc.jrppb.core.config.testutils.ValidatorTestWrapper;
import fr.djaytan.mc.jrppb.core.storage.api.properties.DataSourceType;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PropertiesValidatorTest {

  private PropertiesValidator propertiesValidator;

  @BeforeEach
  void beforeEach() {
    propertiesValidator = new PropertiesValidator(ValidatorTestWrapper.getValidator());
  }

  @Nested
  class WhenValidating {

    @Test
    void withValidValues_shouldSuccess() {
      // Given
      DataSourcePropertiesImpl dataSourcePropertiesImpl =
          new DataSourcePropertiesImpl(
              DataSourceType.MYSQL,
              "patch_place_break",
              new DbmsServerPropertiesImpl(
                  new DbmsHostPropertiesImpl("example.com", 1234, true),
                  new DbmsCredentialsPropertiesImpl("foo", "bar"),
                  "patch_database"),
              new ConnectionPoolPropertiesImpl(60000, 10));

      // When
      ThrowingCallable throwingCallable =
          () -> propertiesValidator.validate(dataSourcePropertiesImpl);

      // Then
      assertThatCode(throwingCallable).doesNotThrowAnyException();
    }

    @Test
    void withInvalidValues_shouldThrowException() {
      // Given
      DataSourcePropertiesImpl dataSourcePropertiesImpl =
          new DataSourcePropertiesImpl(
              DataSourceType.MYSQL,
              "patch_place_break",
              new DbmsServerPropertiesImpl(
                  new DbmsHostPropertiesImpl("example.com", -1, true),
                  new DbmsCredentialsPropertiesImpl("foo", "bar"),
                  "patch_database"),
              new ConnectionPoolPropertiesImpl(60000, 10));

      // When
      Exception exception =
          catchException(() -> propertiesValidator.validate(dataSourcePropertiesImpl));

      // Then
      assertThat(exception).isExactlyInstanceOf(PropertiesValidationException.class);
    }
  }
}
