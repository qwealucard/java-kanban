package tasks;

import savingfiles.TaskType;
import states.TaskState;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private Epic parent;
    private int parentId;

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

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d", id, type, name, state, description, duration, startTime, parentId);
    }
}