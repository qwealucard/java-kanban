package tasks;

import states.TaskState;
public class Subtask extends Task {
    private Epic parent;

    public Subtask(String name, String description, TaskState state, Epic parent) {
        super(name, description, state);
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
}