package tasks;

import savingfiles.TaskType;
import states.TaskState;

public class Subtask extends Task {

    private Epic parent;
    private int parentId;

    public Subtask(int id, TaskType type, String name, TaskState state,String description, int parentId) {
        super(id, type, name, state, description);
        this.parentId = parentId;
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
        int parentId = Integer.parseInt(values[5]);
        Subtask subtask = new Subtask(id, type, name, state, description, parentId);
        return subtask;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d", id, type, name, state, description, parentId);
    }
}