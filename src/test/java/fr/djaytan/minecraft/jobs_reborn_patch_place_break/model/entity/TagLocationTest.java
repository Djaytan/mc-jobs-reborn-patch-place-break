package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity;

import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.TagLocationTestFactory.WORLD_NAME;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.TagLocationTestFactory.X;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.TagLocationTestFactory.Y;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.test.factory.TagLocationTestFactory.Z;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class TagLocationTest {

  @Test
  @DisplayName("Constructor - Nominal case")
  void constructor_nominalCase() {
    TagLocation tagLocation = TagLocation.of(WORLD_NAME, X, Y, Z);

    assertAll("Getters - Verification", () -> assertEquals(WORLD_NAME, tagLocation.getWorldName()),
        () -> assertEquals(X, tagLocation.getX()), () -> assertEquals(Y, tagLocation.getY()),
        () -> assertEquals(Z, tagLocation.getZ()));
  }

  @Test
  @DisplayName("equals() and hashCode() - Verification")
  void equalsAndHashcode_verifier() {
    EqualsVerifier.forClass(TagLocation.class).verify();
  }

  @Test
  @DisplayName("toString() - Verification")
  void toString_verifier() {
    ToStringVerifier.forClass(TagLocation.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
