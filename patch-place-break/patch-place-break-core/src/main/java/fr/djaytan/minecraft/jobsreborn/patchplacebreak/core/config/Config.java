package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import lombok.Value;

@Value(staticConstructor = "of")
public class Config {

  DataSourceProperties dataSource;
}
