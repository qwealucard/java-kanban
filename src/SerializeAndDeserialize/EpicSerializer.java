package SerializeAndDeserialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tasks.Epic;
import tasks.Subtask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class EpicSerializer implements JsonSerializer<Epic> {
    @Override
    public JsonElement serialize(Epic epic, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonEpic = new JsonObject();
        jsonEpic.addProperty("id", epic.getId());
        jsonEpic.addProperty("type", epic.getType().toString());
        jsonEpic.addProperty("name", epic.getName());
        jsonEpic.addProperty("state", epic.getState().toString());
        jsonEpic.addProperty("description", epic.getDescription());
        jsonEpic.add("duration", context.serialize(epic.getDuration(), Duration.class));
        jsonEpic.add("startTime", context.serialize(epic.getStartTime(), LocalDateTime.class));
        return jsonEpic;
    }
}