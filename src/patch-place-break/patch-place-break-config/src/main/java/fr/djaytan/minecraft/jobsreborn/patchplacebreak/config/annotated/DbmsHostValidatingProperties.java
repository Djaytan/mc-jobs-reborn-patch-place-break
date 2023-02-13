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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation.ValidatingConvertibleProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsHostProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Represents an annotated Java Beans version of an {@link DbmsHostProperties}.
 */
@ConfigSerializable
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(staticName = "ofDefault")
@AllArgsConstructor(staticName = "of")
public final class DbmsHostValidatingProperties
    extends ValidatingConvertibleProperties<DbmsHostProperties> {

  /**
   * An hostname cannot exceed 255 characters as per the DNS standard specification.
   *
   * <p/><i/>Note: we explicitly allow most invalid hostnames since otherwise it will require
   * too many work for too limited earnings. In fact, specifying an invalid hostname will
   * be detected by underlying systems (e.g. JDBC). So, we only focus on easy detectable invalid
   * addresses.
   */
  @NotBlank
  @Size(max = 255)
  @Required
  @Comment("Hostname (an IP address (IPv4/IPv6) or a domain name)\n"
      + "Value can't be empty or blank")
  private String hostname = "localhost";

  /**
   * A port cannot exceed 65535, which is the maximum value allowed by the Transport Control
   * Protocol (TCP) and User Datagram Protocol (UDP) standards.
   * The value "0" is excluded since it's a reserved one and must not be used. This is as well
   * an indicator that the port as not been explicitly specified.
   */
  @Max(65535)
  @Positive
  @Required
  @Comment("Port\n" + "Accepted range values: [1-65535]")
  private int port = 3306;

  @Required
  @Comment("Whether an SSL/TLS communication must be established at connection time (more secure)\n"
      + "Only boolean values accepted (true|false)")
  private boolean isSslEnabled = true;

  @Override
  protected @NonNull DbmsHostProperties convertValidated() {
    return DbmsHostProperties.of(hostname, port, isSslEnabled);
  }
}
