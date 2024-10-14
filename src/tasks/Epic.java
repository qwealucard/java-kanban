package tasks;

import savingfiles.TaskType;
import states.TaskState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Epic extends Task {
    private List<Subtask> subtasks;
    protected Duration duration;
    protected LocalDateTime startTime;
    private LocalDateTime endTime;
    private PriorityQueue<Subtask> prioritizedTasks; //PriorityQueue удаляет и добавляет за log(n)

    public Epic(int id, TaskType type, String name, TaskState state, String description, Duration duration, LocalDateTime startTime) {
        super(id, type, name, state, description, duration, startTime);
        this.subtasks = new ArrayList<>();
        this.prioritizedTasks = new PriorityQueue<>(Comparator.comparing(Subtask::getStartTime));
        this.startTime = startTime;
        this.duration = duration;
    }

    public List<Subtask> getPrioritizedTasks() {
        List<Subtask> subtasks = new ArrayList<>(prioritizedTasks);
        return subtasks;
    }

    public void addSubtask(Subtask newSubtask) {
        subtasks.add(newSubtask);
        updateState(newSubtask.getState());
        prioritizedTasks.add(newSubtask);
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

    public void updateFields() {
        duration = calculateDuration();
        startTime = calculateStartTime();
    }

    private Duration calculateDuration() {
        Duration totalDuration = Duration.ZERO;
        for (Subtask subtask : subtasks) {
            totalDuration = totalDuration.plus(subtask.getDuration());
        }
        return totalDuration;
    }

    private LocalDateTime calculateStartTime() {
        if (subtasks.isEmpty()) {
            return null;
        }
        LocalDateTime earliestStartTime = subtasks.get(0).getStartTime();
        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(earliestStartTime)) {
                earliestStartTime = subtask.getStartTime();
            }
        }
        return earliestStartTime;
    }

    private LocalDateTime calculateEndTime() {
        endTime = getEarliestStartTime();
        for (Subtask subtask : subtasks) {
            endTime = endTime.plus(subtask.getDuration());
        }
        return endTime;
    }

    public Duration getTotalDuration() {
        return calculateDuration();
    }

    public LocalDateTime getEarliestStartTime() {
        return calculateStartTime();
    }

    public LocalDateTime getEndTime() {
        return calculateEndTime();
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", id, type, name, state, description, duration, startTime);
    }

    public static Epic fromString(String value) {
        String[] values = value.split(",");
        System.out.println(values.length);
        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskState state = TaskState.valueOf(values[3]);
        String description = values[4];
        Duration duration = Duration.parse(values[5]);
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        return new Epic(id, type, name, state, description, duration, startTime);
    }
}

