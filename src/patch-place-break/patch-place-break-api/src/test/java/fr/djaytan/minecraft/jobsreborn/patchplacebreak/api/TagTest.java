package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import java.time.LocalDateTime;
import java.util.UUID;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagTest {

  @Test
  @DisplayName("Constructor - Successful nominal case")
  void shouldSuccessWhenCreatingWithNominalValues() {
    // Given
    UUID uuid = UUID.randomUUID();
    LocalDateTime localDateTime = LocalDateTime.now();
    boolean isEphemeral = true;

    String worldName = "world";
    int x = 52;
    int y = 68;
    int z = 1254;
    BlockLocation blockLocation = BlockLocation.of(worldName, x, y, z);

    // When
    Tag tag = Tag.of(uuid, localDateTime, isEphemeral, blockLocation);

    // Then
    assertAll(
        "Verification of returned values from getters",
        () -> assertThat(tag.getUuid()).isEqualTo(uuid),
        () -> assertThat(tag.getInitLocalDateTime()).isEqualTo(localDateTime),
        () -> assertThat(tag.isEphemeral()).isEqualTo(isEphemeral),
        () -> assertThat(tag.getBlockLocation()).isEqualTo(blockLocation));
  }

  @Test
  @DisplayName("equals() & hashCode() - Verifications")
  void equalsAndHashcodeContractVerification() {
    EqualsVerifier.forClass(Tag.class).verify();
  }

  @Test
  @DisplayName("toString() - Verifications")
  void toStringContractVerification() {
    ToStringVerifier.forClass(Tag.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
