package tasks;

import savedTask.TaskType;
import states.TaskState;

public class Task {
    protected String name;
    protected String description;
    protected TaskState state;
    protected int id;
    protected TaskType type;

    public Task(int id, TaskType type, String name, TaskState state, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.state = state;
        this.description = description;
    }

    public void updateState(TaskState newState) {
        state = newState;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskState getState() {
        return state;
    }

    public TaskType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + state + "," + description;
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskState state = TaskState.valueOf(values[3]);
        String description = values[4];
        return new Task(id, type, name, state, description);
    }
}



