package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Represents a convertible data structure after validation which provides a way to convert it to
 * its final form.
 *
 * <p>This approach allows for decoupling the validation and the final data structure creation.
 *
 * @param <T> The type of the final data structure.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public abstract class ValidatingConvertibleProperties<T> {

  /** Marker indicating if the implementer's properties has been validated or not. */
  @SuppressWarnings("java:S2065") // 'transient' keyword required for serialization exclusion
  private transient boolean isValidated;

  /** Marks the properties as validated. */
  public void markAsValidated() {
    this.isValidated = true;
  }

  /**
   * Converts the properties to the "ready to use" data structure version.
   *
   * @return The "ready to use" data structure version.
   * @throws IllegalStateException if properties have not been validated before the conversion.
   */
  public @NonNull T convert() {
    if (!isValidated) {
      throw new IllegalStateException("Properties must be validated before being converted");
    }
    return convertValidated();
  }

  /**
   * Converts the data from the current instance of the class inheriting this.
   *
   * <p><i>Note: This method must be called only after the data structure validation.
   *
   * @return The converted final data structure.
   */
  protected abstract @NonNull T convertValidated();
}
