package memory;

import history.InMemoryHistoryManager;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private final List<Task> prioritizedTasks = new ArrayList<>();
    private int currentId = 0;

    public boolean isOverlapping(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(task1.getDuration());
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(task2.getDuration());
        return (start1.isBefore(end2) && end1.isAfter(start2));
    }

    private List<Task> addToPrioritizedTasks() {
        List<Task> prioritizedTasks = new ArrayList<>();
        prioritizedTasks.addAll(tasks.values());

        prioritizedTasks.addAll(epics.values().stream()
                                     .flatMap(epic -> epic.getPrioritizedSubtasks().stream())
                                     .collect(Collectors.toList()));

        prioritizedTasks.sort(Comparator.comparing(Task::getStartTime));
        return prioritizedTasks;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return addToPrioritizedTasks();
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
        getPrioritizedTasks().removeIf(task -> tasks.containsKey(task.getId()));
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().stream()
                .forEach(historyManager::remove);
        subtasks.keySet().stream()
                .forEach(subtasks::remove);
        subtasks.clear();
        getPrioritizedTasks().removeIf(task -> subtasks.containsKey(task.getId()));
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.keySet().stream()
             .forEach(historyManager::remove);
        epics.keySet().stream()
             .forEach(epics::remove);
        epics.clear();
       getPrioritizedTasks().removeIf(task -> epics.containsKey(task.getId()) || subtasks.containsKey(task.getId()));
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
        addToPrioritizedTasks();
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
        addToPrioritizedTasks();
        return newTask.getId();
    }

    @Override
    public void updateTask(Epic updatedEpic) {
        Epic tempLink = epics.get(updatedEpic.getId());
        tempLink.setName(updatedEpic.getName());
        tempLink.setDescription(updatedEpic.getDescription());
        updatePrioritizedTasksForEpic(updatedEpic);
    }

    @Override
    public void updateTask(Subtask updatedSubtask) {
        Subtask tempLink = subtasks.get(updatedSubtask.getId());
        tempLink.setName(updatedSubtask.getName());
        tempLink.setDescription(updatedSubtask.getDescription());
        tempLink.updateState(updatedSubtask.getState());
        updatePrioritizedTasksForSubtask(updatedSubtask);
    }

    @Override
    public void updateTask(Task updatedTask) {
        Task tempLink = tasks.get(updatedTask.getId());
        tempLink.setName(updatedTask.getName());
        tempLink.setDescription(updatedTask.getDescription());
        tempLink.updateState(updatedTask.getState());
        updatePrioritizedTasksForTask(updatedTask);
    }

    private void updatePrioritizedTasksForTask(Task task) {
        prioritizedTasks.remove(task);
        prioritizedTasks.add(task);
        Set<Task> sortedSubtasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedSubtasks.add(task);
        prioritizedTasks.remove(task);
        prioritizedTasks.addAll(sortedSubtasks);
    }

    private void updatePrioritizedTasksForSubtask(Subtask subtask) {
        prioritizedTasks.remove(subtask);
        prioritizedTasks.add(subtask);
        Set<Task> sortedSubtasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedSubtasks.add(subtask);
        prioritizedTasks.remove(subtask);
        prioritizedTasks.addAll(sortedSubtasks);
    }

    private void updatePrioritizedTasksForEpic(Epic epic) {
        prioritizedTasks.removeAll(epic.getSubtasks());
        prioritizedTasks.addAll(epic.getSubtasks());
        Set<Task> sortedSubtasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedSubtasks.addAll(epic.getSubtasks());
        prioritizedTasks.removeAll(epic.getSubtasks());
        prioritizedTasks.addAll(sortedSubtasks);
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
