package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Singleton;
import lombok.NonNull;

@Singleton
public class LocalDateTimeStringSerializer implements StringSerializer<LocalDateTime> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

  @Override
  public @NonNull String serialize(@NonNull LocalDateTime localDateTime) {
    return localDateTime.format(formatter);
  }

  @Override
  public @NonNull LocalDateTime deserialize(@NonNull String string) {
    return LocalDateTime.parse(string, formatter);
  }
}
