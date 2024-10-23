package SerializeAndDeserialize;

import com.google.gson.*;
import savingfiles.TaskType;
import states.TaskState;
import tasks.Epic;
import tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        TaskType type = TaskType.valueOf(jsonObject.get("type").getAsString());
        String name = jsonObject.get("name").getAsString();
        TaskState state = TaskState.valueOf(jsonObject.get("state").getAsString());
        String description = jsonObject.get("description").getAsString();
        Duration duration = context.deserialize(jsonObject.get("duration"), Duration.class);
        LocalDateTime startTime = context.deserialize(jsonObject.get("startTime"), LocalDateTime.class);
        return new Task(id, type, name, state, description, duration, startTime);
    }

}
