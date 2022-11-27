package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity;

import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.PatchPlaceAndBreakTagTestFactory.INIT_LOCAL_DATE_TIME;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.PatchPlaceAndBreakTagTestFactory.IS_EPHEMERAL;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.PatchPlaceAndBreakTagTestFactory.PATCH_UUID;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.PatchPlaceAndBreakTagTestFactory.TAG_LOCATION;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class PatchPlaceAndBreakTagTest {

  @Test
  @DisplayName("Constructor - Nominal case")
  void constructor_nominalCase() {
    PatchPlaceAndBreakTag tag =
        PatchPlaceAndBreakTag.of(PATCH_UUID, INIT_LOCAL_DATE_TIME, IS_EPHEMERAL, TAG_LOCATION);

    assertAll("Getters - Verification", () -> assertEquals(PATCH_UUID, tag.getUuid()),
        () -> assertEquals(INIT_LOCAL_DATE_TIME, tag.getInitLocalDateTime()),
        () -> assertEquals(IS_EPHEMERAL, tag.isEphemeral()),
        () -> assertEquals(TAG_LOCATION, tag.getTagLocation()));
  }

  @Test
  @DisplayName("equals() and hashCode() - Verification")
  void equalsAndHashcode_verifier() {
    EqualsVerifier.forClass(PatchPlaceAndBreakTag.class).verify();
  }

  @Test
  @DisplayName("toString() - Verification")
  void toString_verifier() {
    ToStringVerifier.forClass(PatchPlaceAndBreakTag.class).withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
