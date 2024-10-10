package memory;

import history.InMemoryHistoryManager;
import interfaces.TaskManager;
import savingfiles.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
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
    public void deleteAllTasks() throws ManagerSaveException {
        List<Integer> taskId = new ArrayList<>(tasks.keySet());
        for (int i = taskId.size() - 1; i >= 0; i--) {
            historyManager.remove(taskId.get(i));
            tasks.remove(taskId.get(i));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() throws ManagerSaveException {
        for (Integer subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
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
        subtasks.put(newSubtask.getId(), newSubtask);
        historyManager.add(newSubtask);
        return newSubtask.getId();
    }

    @Override
    public int addNewTask(Task newTask) {
        newTask.setId(currentId);
        currentId++;
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
        for (Subtask subtask : tempEpic.getSubtasks()) {
            deleteSubtaskById(subtask.getId());
        }
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
