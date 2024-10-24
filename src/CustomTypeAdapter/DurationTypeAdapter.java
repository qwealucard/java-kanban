package CustomTypeAdapter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter out, Duration duration) throws IOException {
        out.value(duration.toHours());
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Duration.ofHours(Integer.parseInt(jsonReader.nextString()));
    }
}