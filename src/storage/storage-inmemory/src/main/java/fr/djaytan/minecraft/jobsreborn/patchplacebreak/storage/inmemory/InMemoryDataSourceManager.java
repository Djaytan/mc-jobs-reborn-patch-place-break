package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.inmemory;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.DataSourceManager;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class InMemoryDataSourceManager implements DataSourceManager {

  @Override
  public void connect() {
    log.atInfo().log("Nothing to do when connecting to the InMemory datasource.");
  }

  @Override
  public void disconnect() {
    log.atInfo().log("Nothing to do when disconnecting from the InMemory datasource.");
  }
}
