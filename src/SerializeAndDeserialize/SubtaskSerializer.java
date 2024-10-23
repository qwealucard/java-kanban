package SerializeAndDeserialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskSerializer implements JsonSerializer<Subtask> {
    @Override
    public JsonElement serialize(Subtask subtask, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonSubtask = new JsonObject();
        jsonSubtask.addProperty("id", subtask.getId());
        jsonSubtask.addProperty("type", subtask.getType().toString());
        jsonSubtask.addProperty("name", subtask.getName());
        jsonSubtask.addProperty("state", subtask.getState().toString());
        jsonSubtask.addProperty("description", subtask.getDescription());
        jsonSubtask.add("duration", context.serialize(subtask.getDuration(), Duration.class));
        jsonSubtask.add("startTime", context.serialize(subtask.getStartTime(), LocalDateTime.class));
        jsonSubtask.addProperty("parentId", subtask.getParentId());
        return jsonSubtask;
    }
}