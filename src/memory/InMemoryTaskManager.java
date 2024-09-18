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
        for (Integer task : tasks.keySet()) {
            historyManager.removeHistory(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer subtask : subtasks.keySet()) {
            historyManager.removeHistory(subtask);
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
            historyManager.removeHistory(epic);
        }
        epics.clear();
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addToHistory(task);
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addToHistory(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.addToHistory(subtask);
        }
        return subtask;
    }

    @Override
    public int addNewTask(Epic newEpic) {
        newEpic.setId(currentId);
        currentId++;
        epics.put(newEpic.getId(), newEpic);
        historyManager.addToHistory(newEpic);
        return newEpic.getId();
    }

    @Override
    public int addNewTask(Subtask newSubtask) {
        newSubtask.setId(currentId);
        currentId++;
        newSubtask.getParent().addSubtask(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        historyManager.addToHistory(newSubtask);
        return newSubtask.getId();
    }

    @Override
    public int addNewTask(Task newTask) {
        newTask.setId(currentId);
        currentId++;
        tasks.put(newTask.getId(), newTask);
        historyManager.addToHistory(newTask);
        return newTask.getId();
    }

    @Override
    public int updateTask(Epic updatedEpic) {
        Epic tempLink = epics.get(updatedEpic.getId());
        tempLink.setName(updatedEpic.getName());
        tempLink.setDescription(updatedEpic.getDescription());
        return tempLink.getId();
    }

    @Override
    public int updateTask(Subtask updatedSubtask) {
        Subtask tempLink = subtasks.get(updatedSubtask.getId());
        tempLink.setName(updatedSubtask.getName());
        tempLink.setDescription(updatedSubtask.getDescription());
        tempLink.updateState(updatedSubtask.getState());
        return tempLink.getId();
    }

    @Override
    public int updateTask(Task updatedTask) {
        Task tempLink = tasks.get(updatedTask.getId());
        tempLink.setName(updatedTask.getName());
        tempLink.setDescription(updatedTask.getDescription());
        tempLink.updateState(updatedTask.getState());
        return tempLink.getId();
    }

    @Override
    public Subtask deleteSubtaskById(int id) {
        Subtask tempLink = subtasks.remove(id);
        tempLink.getParent().removeSubtask(tempLink);
        historyManager.removeHistory(id);
        return tempLink;
    }

    @Override
    public Epic deleteEpicById(int id) {
        Epic tempLink = epics.remove(id);
        for (Subtask subtask : tempLink.getSubtasks()) {
            deleteSubtaskById(subtask.getId());
            historyManager.removeHistory(id);
        }
        return tempLink;
    }

    @Override
    public Task deleteTaskById(int id) {
        Task tempLink = tasks.remove(id);
        historyManager.removeHistory(id);
        return tempLink;
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }

    public InMemoryHistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryList();
    }
}
