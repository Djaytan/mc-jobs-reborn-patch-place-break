package fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities;

import lombok.Value;

@Value(staticConstructor = "of")
public class Vector {

  int modX;
  int modY;
  int modZ;
}
