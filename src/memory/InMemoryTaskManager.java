package memory;

import history.InMemoryHistoryManager;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private int currentId = 0;

    @Override
    public boolean isOverlapping(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(task1.getDuration());
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(task2.getDuration());
        return (start1.isBefore(end2) && end1.isAfter(start2));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> prioritizedTasks = new ArrayList<>();
        prioritizedTasks.addAll(tasks.values());
        epics.values().stream()
                        .flatMap(epic -> epic.getPrioritizedTasks().stream())
                     .forEach(prioritizedTasks::add);
        Collections.sort(prioritizedTasks, Comparator.comparing(Task::getStartTime));
        return prioritizedTasks;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList(tasks.values());
    }

    @Override
    public List<Task> getAllEpics() {
        return new ArrayList(epics.values());
    }

    @Override
    public List<Task> getAllSubtasks() {
        return new ArrayList(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        List<Integer> taskId = new ArrayList<>(tasks.keySet());
        for (int i = taskId.size() - 1; i >= 0; i--) {
            historyManager.remove(taskId.get(i));
            tasks.remove(taskId.get(i));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().stream()
                .forEach(historyManager::remove);
        subtasks.keySet().stream()
                .forEach(subtasks::remove);
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.keySet().stream()
             .forEach(historyManager::remove);
        epics.keySet().stream()
             .forEach(epics::remove);
        epics.clear();
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public int addNewTask(Epic newEpic) {
        newEpic.setId(currentId);
        currentId++;
        if (getAllEpics().stream()
                 .anyMatch(existingTask -> isOverlapping(newEpic, existingTask))) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        epics.put(newEpic.getId(), newEpic);
        historyManager.add(newEpic);
        return newEpic.getId();
    }

    @Override
    public int addNewTask(Subtask newSubtask) {
        newSubtask.setId(currentId);
        currentId++;
        if (getAllSubtasks().stream()
                         .anyMatch(existingTask -> isOverlapping(newSubtask, existingTask))) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        subtasks.put(newSubtask.getId(), newSubtask);
        historyManager.add(newSubtask);
        return newSubtask.getId();
    }

    @Override
    public int addNewTask(Task newTask) {
        newTask.setId(currentId);
        currentId++;
        if (getAllTasks().stream()
                 .anyMatch(existingTask -> isOverlapping(newTask, existingTask))) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        tasks.put(newTask.getId(), newTask);
        historyManager.add(newTask);
        return newTask.getId();
    }

    @Override
    public void updateTask(Epic updatedEpic) {
        Epic tempLink = epics.get(updatedEpic.getId());
        tempLink.setName(updatedEpic.getName());
        tempLink.setDescription(updatedEpic.getDescription());
    }

    @Override
    public void updateTask(Subtask updatedSubtask) {
        Subtask tempLink = subtasks.get(updatedSubtask.getId());
        tempLink.setName(updatedSubtask.getName());
        tempLink.setDescription(updatedSubtask.getDescription());
        tempLink.updateState(updatedSubtask.getState());
    }

    @Override
    public void updateTask(Task updatedTask) {
        Task tempLink = tasks.get(updatedTask.getId());
        tempLink.setName(updatedTask.getName());
        tempLink.setDescription(updatedTask.getDescription());
        tempLink.updateState(updatedTask.getState());
    }

    @Override
    public void deleteTaskById(int id) {
        Task tempTask = tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic tempEpic = epics.remove(id);
        tempEpic.getSubtasks().stream()
                .forEach(subtask -> deleteSubtaskById(subtask.getId()));
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask tempSubtask = subtasks.remove(id);
        tempSubtask.getParent().removeSubtask(tempSubtask);
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getViewedTaskHistory();
    }

}
