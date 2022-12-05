package fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.inmemory;

import com.google.inject.AbstractModule;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.persistence.api.PatchPlaceAndBreakTagDao;

public class GuicePersistenceInmemoryModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PatchPlaceAndBreakTagDao.class).to(PatchPlaceAndBreakTagMemoryDao.class);
  }
}
