package fr.djaytan.minecraft.jobs_reborn_patch_place_break.model.entity;

import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.TagLocationStubFactory.WORLD_NAME;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.TagLocationStubFactory.X;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.TagLocationStubFactory.Y;
import static fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.TagLocationStubFactory.Z;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobs_reborn_patch_place_break.lib.test.factory.TagLocationStubFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

class TagLocationTest {

  @Test
  @DisplayName("Constructor - Nominal case")
  void constructor_nominalCase() {
    TagLocation expectedTagLocation = TagLocationStubFactory.create();

    TagLocation actualTagLocation = TagLocation.of(WORLD_NAME, X, Y, Z);

    assertEquals(expectedTagLocation, actualTagLocation);
    assertAll("Getters - Verification",
        () -> assertEquals(WORLD_NAME, actualTagLocation.getWorldName()),
        () -> assertEquals(X, actualTagLocation.getX()),
        () -> assertEquals(Y, actualTagLocation.getY()),
        () -> assertEquals(Z, actualTagLocation.getZ()));
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
