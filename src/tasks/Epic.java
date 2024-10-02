package tasks;

import savedTask.TaskType;
import states.TaskState;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<Subtask>();

    public Epic(int id, TaskType type, String name, TaskState state, String description) {
        super(id, type, name, state, description);
    }

    public void addSubtask(Subtask newSubtask) {
        subtasks.add(newSubtask);
        updateState(newSubtask.getState());
    }

    public void removeSubtask(Subtask subtask) {
        if (subtasks.contains(subtask)) {
            subtasks.remove(subtask);
            updateState(TaskState.NEW);
        }
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        updateState(TaskState.NEW);
    }

    private TaskState checkState() {
        for (int i = 1; i < subtasks.size(); i++) {
            if (subtasks.get(i).getState() != subtasks.get(i - 1).getState()) {
                return TaskState.IN_PROGRESS;
            }
        }
        if (!subtasks.isEmpty()) {
            return subtasks.getFirst().state;
        } else {
            return TaskState.NEW;
        }
    }

    @Override
    public void updateState(TaskState newState) {
        state = checkState();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + state + "," + description + ",";
    }
}

