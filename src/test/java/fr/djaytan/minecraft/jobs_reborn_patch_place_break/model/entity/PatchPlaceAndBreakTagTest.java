package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity;

import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.PatchPlaceAndBreakTagStubFactory.INIT_LOCAL_DATE_TIME;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.PatchPlaceAndBreakTagStubFactory.IS_EPHEMERAL;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.PatchPlaceAndBreakTagStubFactory.PATCH_UUID;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.PatchPlaceAndBreakTagStubFactory.TAG_LOCATION;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.PatchPlaceAndBreakTagStubFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

class PatchPlaceAndBreakTagTest {

  @Test
  @DisplayName("Constructor - Nominal case")
  void constructor_nominalCase() {
    PatchPlaceAndBreakTag expectedTag = PatchPlaceAndBreakTagStubFactory.create();

    PatchPlaceAndBreakTag actualTag =
        PatchPlaceAndBreakTag.of(PATCH_UUID, INIT_LOCAL_DATE_TIME, IS_EPHEMERAL, TAG_LOCATION);

    assertEquals(expectedTag, actualTag);
    assertAll("Getters - Verification", () -> assertEquals(PATCH_UUID, actualTag.getUuid()),
        () -> assertEquals(INIT_LOCAL_DATE_TIME, actualTag.getInitLocalDateTime()),
        () -> assertEquals(IS_EPHEMERAL, actualTag.isEphemeral()),
        () -> assertEquals(TAG_LOCATION, actualTag.getTagLocation()));
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
