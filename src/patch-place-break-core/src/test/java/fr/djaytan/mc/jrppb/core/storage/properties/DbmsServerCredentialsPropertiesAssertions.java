package fr.djaytan.mc.jrppb.core.storage.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jetbrains.annotations.NotNull;

public final class DbmsServerCredentialsPropertiesAssertions {

  public static void assertSuccessfulInstantiation(
      @NotNull String username, @NotNull String password) {
    assertThat(new DbmsServerCredentialsProperties(username, password))
        .satisfies(v -> assertThat(v.username()).isEqualTo(username))
        .satisfies(v -> assertThat(v.password()).isEqualTo(password));
  }

  public static void assertInstantiationFailureWithBlankUsername(@NotNull String username) {
    assertThatThrownBy(
            () ->
                new DbmsServerCredentialsProperties(
                    username,
                    DbmsServerCredentialsPropertiesTestDataSet.NOMINAL_DBMS_SERVER_PASSWORD))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("The DBMS server username cannot be blank");
  }
}
