package memory;

import history.InMemoryHistoryManager;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private int currentId = 0;

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
        for (Integer subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        for (Integer epic : epics.keySet()) {
            historyManager.remove(epic);
        }
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
    public void addNewTask(Epic newEpic) {
        newEpic.setId(currentId);
        currentId++;
        epics.put(newEpic.getId(), newEpic);
        historyManager.add(newEpic);
    }

    @Override
    public void addNewTask(Subtask newSubtask) {
        newSubtask.setId(currentId);
        currentId++;
        newSubtask.getParent().addSubtask(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        historyManager.add(newSubtask);
    }

    @Override
    public void addNewTask(Task newTask) {
        newTask.setId(currentId);
        currentId++;
        tasks.put(newTask.getId(), newTask);
        historyManager.add(newTask);
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
        if (tasks.containsKey(id)) {
            Task tempTask = tasks.remove(id);
            historyManager.remove(id);
        } else if (epics.containsKey(id)) {
            Epic tempEpic = epics.remove(id);
            for (Subtask subtask : tempEpic.getSubtasks()) {
                deleteTaskById(subtask.getId());
            }
            historyManager.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask tempSubtask = subtasks.remove(id);
            tempSubtask.getParent().removeSubtask(tempSubtask);
            historyManager.remove(id);
        } else {
            System.out.println("Task or Epic or Subtask with id " + id + " not found");
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getViewedTaskHistory();
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}
