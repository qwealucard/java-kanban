package CustomTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import savingfiles.TaskType;
import states.TaskState;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskTypeAdapter extends TypeAdapter<Task> {

    private final Gson gson;

    public TaskTypeAdapter(Gson gson) {
        this.gson = gson;
    }
    @Override
    public void write(JsonWriter out, Task task) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", task.getId());
        jsonObject.addProperty("type", task.getType().name());
        jsonObject.addProperty("name", task.getName());
        jsonObject.addProperty("state", task.getState().name());
        jsonObject.addProperty("description", task.getDescription());

        jsonObject.add("duration", gson.toJsonTree(task.getDuration()));
        jsonObject.add("startTime", gson.toJsonTree(task.getStartTime()));

        out.jsonValue(jsonObject.toString());
    }

    @Override
    public Task read(JsonReader in) throws IOException {
        JsonObject jsonObject = gson.fromJson(in, JsonObject.class);

        int id = jsonObject.get("id").getAsInt();
        TaskType type = TaskType.valueOf(jsonObject.get("type").getAsString());
        String name = jsonObject.get("name").getAsString();
        TaskState state = TaskState.valueOf(jsonObject.get("state").getAsString());
        String description = jsonObject.get("description").getAsString();

        Duration duration = gson.fromJson(jsonObject.get("duration"), Duration.class);
        LocalDateTime startTime = gson.fromJson(jsonObject.get("startTime"), LocalDateTime.class);

        return new Task(id, type, name, state, description, duration, startTime);
    }
}
