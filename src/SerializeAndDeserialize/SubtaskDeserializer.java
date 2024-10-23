package SerializeAndDeserialize;

import com.google.gson.*;
import savingfiles.TaskType;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskDeserializer implements JsonDeserializer<Subtask> {
    @Override
    public Subtask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        TaskType type = TaskType.valueOf(jsonObject.get("type").getAsString());
        String name = jsonObject.get("name").getAsString();
        TaskState state = TaskState.valueOf(jsonObject.get("state").getAsString());
        String description = jsonObject.get("description").getAsString();
        Duration duration = context.deserialize(jsonObject.get("duration"), Duration.class);
        LocalDateTime startTime = context.deserialize(jsonObject.get("startTime"), LocalDateTime.class);
        int parentId = jsonObject.get("parentId").getAsInt();
        return new Subtask(id, type, name, state, description, duration, startTime, parentId);
    }
}
