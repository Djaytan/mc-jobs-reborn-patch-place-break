/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class DataSourcePropertiesTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    DbmsServerProperties dbmsServerProperties =
        DbmsServerProperties.of(DbmsHostProperties.of("host", 80, true),
            CredentialsProperties.of("username", "passwoord"), "testdb");
    ConnectionPoolProperties connectionPoolProperties = ConnectionPoolProperties.of(120, 2);
    DataSourceType dataSourceType = DataSourceType.MYSQL;
    String table = "test";


    // When
    DataSourceProperties dataSourceProperties = DataSourceProperties.of(dataSourceType, table,
        dbmsServerProperties, connectionPoolProperties);

    // Then
    assertAll("Verification of returned values from getters",
        () -> assertThat(dataSourceProperties.getType()).isEqualTo(dataSourceType),
        () -> assertThat(dataSourceProperties.getTable()).isEqualTo(table),
        () -> assertThat(dataSourceProperties.getDbmsServer()).isEqualTo(dbmsServerProperties),
        () -> assertThat(dataSourceProperties.getConnectionPool())
            .isEqualTo(connectionPoolProperties));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(DataSourceProperties.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(DataSourceProperties.class).withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
