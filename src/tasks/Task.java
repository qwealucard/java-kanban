package tasks;

import savingfiles.TaskType;
import states.TaskState;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String description;
    protected TaskState state;
    protected int id;
    protected TaskType type;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(int id, TaskType type, String name, TaskState state, String description, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.state = state;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
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

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }


    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", id, type, name, state, description, duration, startTime);
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskState state = TaskState.valueOf(values[3]);
        String description = values[4];
        Duration duration = Duration.parse(values[5]);
        LocalDateTime startTime = LocalDateTime.parse(values[6]);

        if (values.length > 7) {
            int parentId = Integer.parseInt(values[7]);
            return new Subtask(id, type, name, state, description, duration, startTime, parentId);
        } else {
            if (type == TaskType.EPIC) {
                return new Epic(id, type, name, state, description, duration, startTime);
            } else {
                return new Task(id, type, name, state, description, duration, startTime);
            }
        }
    }
}



