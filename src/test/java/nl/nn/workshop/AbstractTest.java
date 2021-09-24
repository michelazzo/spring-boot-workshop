package nl.nn.workshop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import nl.nn.workshop.utils.LocalDateDeserializer;
import nl.nn.workshop.utils.LocalDateSerializer;
import nl.nn.workshop.utils.LocalDateTimeDeserializer;
import nl.nn.workshop.utils.LocalDateTimeSerializer;

public abstract class AbstractTest {

  protected static final Gson GSON;

  static {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
    GSON = gsonBuilder.setPrettyPrinting().create();
  }

}
