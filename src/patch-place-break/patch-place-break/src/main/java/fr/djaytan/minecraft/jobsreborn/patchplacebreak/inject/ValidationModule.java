package fr.djaytan.minecraft.jobsreborn.patchplacebreak.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.NonNull;

/** Represents validation related configs. */
public class ValidationModule extends AbstractModule {

  @Provides
  @Singleton
  public @NonNull ValidatorFactory provideValidatorFactory() {
    return Validation.buildDefaultValidatorFactory();
  }

  @Provides
  @Singleton
  public @NonNull Validator provideValidator(ValidatorFactory validatorFactory) {
    return validatorFactory.getValidator();
  }
}
