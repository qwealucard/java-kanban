package tasks;

import savingfiles.TaskType;
import states.TaskState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(int id, TaskType type, String name, TaskState state, String description, Duration duration, LocalDateTime startTime) {
        super(id, type, name, state, description, duration, startTime);
        this.subtasks = new ArrayList<>();
        this.startTime = startTime;
        this.duration = duration;
    }

    public void addNewTask(Subtask newSubtask) {
        subtasks.add(newSubtask);
        updateState(newSubtask.getState());
    }

    public void removeSubtask(Subtask subtask) {
        if (subtasks.contains(subtask)) {
            subtasks.remove(subtask);
            updateState(TaskState.NEW);
        }
    }

    public void deleteAllSubtasks() {
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

    private Duration calculateDuration() {
        return subtasks.stream()
                       .map(Subtask::getDuration)
                       .reduce(Duration.ZERO, Duration::plus);
    }

    private LocalDateTime calculateStartTime() {
        return subtasks.stream()
                       .map(Subtask::getStartTime)
                       .min(LocalDateTime::compareTo)
                       .orElse(null);
    }

    private LocalDateTime calculateEndTime() {
        return subtasks.stream()
                       .max(Comparator.comparing(Subtask::getStartTime))
                       .map(Subtask::getEndTime)
                       .orElse(null);
    }

    public Duration getDuration() {
        return calculateDuration();
    }

    public LocalDateTime getStartTime() {
        return calculateStartTime();
    }

    public LocalDateTime getEndTime() {
        return calculateEndTime();
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", id, type, name, state, description, duration, startTime);
    }
}

