package tasks;

import savingfiles.TaskType;
import states.TaskState;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private Epic parent;
    private int parentId;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Subtask(int id, TaskType type, String name, TaskState state,String description,Duration duration, LocalDateTime startTime, int parentId) {
        super(id, type, name, state, description, duration, startTime);
        this.parentId = parentId;
        this.duration = duration;
        this.startTime = startTime;
    }

    @Override
    public void updateState(TaskState newState) {
        state = newState;
        parent.updateState(newState);
    }

    public Epic getParent() {
        return parent;
    }

    public int getParentId() {
        return parentId;
    }

    public static Subtask fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskState state = TaskState.valueOf(values[3]);
        String description = values[4];
        Duration duration = Duration.parse(values[5]);
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        int parentId = Integer.parseInt(values[7]);
        Subtask subtask = new Subtask(id, type, name, state, description, duration, startTime, parentId);
        return subtask;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d", id, type, name, state, description, duration, startTime, parentId);
    }
}