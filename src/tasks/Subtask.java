package tasks;

import SavedTask.TaskType;
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

    public static Subtask fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        String description = values[3];
        TaskState state = TaskState.valueOf(values[4]);
        Epic parent = Epic.fromString(values[5]);

        return new Subtask(id, type, name, state, description, parent);
    }
}