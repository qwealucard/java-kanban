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
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int currentId = 0;

    private boolean isOverlapping(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(task1.getDuration());
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(task2.getDuration());
        return (start1.isBefore(end2) && end1.isAfter(start2));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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
        List<Integer> taskId = new ArrayList<>(tasks.keySet());//Здесь требуется удаление с конца, чтобы не столкнуться с ошибкой
        for (int i = taskId.size() - 1; i >= 0; i--) { //java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
            historyManager.remove(taskId.get(i));//т.к. при удалении наш список будет сдвигаться в сторону 0, а ForEach этого не учитывает
        }
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        List<Integer> subtaskId = new ArrayList<>(subtasks.keySet());
        for (int i = subtaskId.size() - 1; i >= 0; i--) {
            historyManager.remove(subtaskId.get(i));
        }
        subtasks.values().forEach(prioritizedTasks::remove);
        epics.values().forEach(Epic::deleteAllSubtasks);
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
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

    private boolean isIntersection(Task newTask) {
        return prioritizedTasks.stream()
                               .anyMatch(existingTask -> isOverlapping(newTask, existingTask));
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
        int epicId = newSubtask.getParentId();
        if (isIntersection(newSubtask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        newSubtask.setId(currentId);
        currentId++;
        Epic epic = epics.get(epicId);
        epic.addNewTask(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        historyManager.add(newSubtask);
        prioritizedTasks.add(newSubtask);
        return newSubtask.getId();
    }

    @Override
    public int addNewTask(Task newTask) {
        if (isIntersection(newTask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        newTask.setId(currentId);
        currentId++;
        tasks.put(newTask.getId(), newTask);
        historyManager.add(newTask);
        prioritizedTasks.add(newTask);
        return newTask.getId();
    }

    @Override
    public void updateTask(Epic updatedEpic) {
        if (!tasks.containsKey(updatedEpic.getId())) {
            throw new IllegalArgumentException("No epic by id=%s".formatted(updatedEpic.getId()));
        }
        Epic tempLink = epics.get(updatedEpic.getId());
        tempLink.setName(updatedEpic.getName());
        tempLink.setDescription(updatedEpic.getDescription());
    }

    @Override
    public void updateTask(Subtask updatedSubtask) {

        Subtask savedSubtask = subtasks.get(updatedSubtask.getId());
        savedSubtask.setName(savedSubtask.getName());
        savedSubtask.setDescription(updatedSubtask.getDescription());
        savedSubtask.updateState(updatedSubtask.getState());
        Epic epic = epics.get(savedSubtask.getParentId());
        epic.updateState(updatedSubtask.getState());
        epic.removeSubtask(savedSubtask);
        epic.addNewTask(updatedSubtask);
        prioritizedTasks.remove(savedSubtask);
        if (isIntersection(updatedSubtask)) {
            prioritizedTasks.add(savedSubtask);
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        prioritizedTasks.add(updatedSubtask);
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (!tasks.containsKey(updatedTask.getId())) {
            throw new IllegalArgumentException("No task by id=%s".formatted(updatedTask.getId()));
        }
        Task savedTask = tasks.get(updatedTask.getId());
        savedTask.setName(updatedTask.getName());
        savedTask.setDescription(updatedTask.getDescription());
        savedTask.updateState(updatedTask.getState());
        prioritizedTasks.remove(savedTask);
        if (isIntersection(updatedTask)) {
            prioritizedTasks.add(savedTask);
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        prioritizedTasks.add(updatedTask);
    }

    @Override
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(getTaskByID(id));
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
        prioritizedTasks.remove(getSubtaskByID(id));
        Subtask tempSubtask = subtasks.remove(id);
        Epic parent = getEpicByID(tempSubtask.getParentId());
        parent.removeSubtask(tempSubtask);
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
