package tasks;

import saves.TaskType;
import states.TaskState;

public class Subtask extends Task {
    private static Epic parent;

    public Subtask(int id, TaskType type, String name, TaskState state,String description, Epic parent) {
        super(id, type, name, state, description);
        this.parent = parent;
    }

    @Override
    public void updateState(TaskState newState) {
        state = newState;
        parent.updateState(newState);
    }

    public Epic getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + state + "," + description + "," + parent;
    }
}