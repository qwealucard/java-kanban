package memory;

import history.InMemoryHistoryManager;
import interface_class.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
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
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
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
            if (historyManager.getViewedTaskHistory().size() > 10) {
                historyManager.getViewedTaskHistory().removeLast();
            }
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            if (historyManager.getViewedTaskHistory().size() > 10) {
                historyManager.getViewedTaskHistory().removeLast();
            }
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);;
            if (historyManager.getViewedTaskHistory().size() > 10) {
                historyManager.getViewedTaskHistory().removeLast();
            }
        }
        return subtask;
    }

    @Override
    public int addNewEpic(Epic newEpic) {
        newEpic.setId(currentId);
        epics.put(newEpic.getId(), newEpic);
        currentId++;
        return newEpic.getId();
    }

    @Override
    public int addNewSubtask(Subtask newSubtask) {
        newSubtask.setId(currentId);
        newSubtask.getParent().addSubtask(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        currentId++;
        return newSubtask.getId();
    }

    @Override
    public int addNewTask(Task newTask) {
        // на всякий случай, защита от дурака
        if (newTask instanceof Subtask) {
            return addNewSubtask((Subtask) newTask);
        } else if (newTask instanceof Epic) {
            return addNewEpic((Epic) newTask);
        } else {
            currentId++;
            newTask.setId(currentId);
            tasks.put(newTask.getId(), newTask);
            return newTask.getId();
        }
    }

    @Override
    public int updateEpic(Epic updatedEpic) {
        Epic tempLink = epics.get(updatedEpic.getId());
        tempLink.setName(updatedEpic.getName());
        tempLink.setDescription(updatedEpic.getDescription());
        return tempLink.getId();
    }

    @Override
    public int updateSubtask(Subtask updatedSubtask) {
        Subtask tempLink = subtasks.get(updatedSubtask.getId());
        tempLink.setName(updatedSubtask.getName());
        tempLink.setDescription(updatedSubtask.getDescription());
        tempLink.updateState(updatedSubtask.getState());
        return tempLink.getId();
    }

    @Override
    public int updateTask(Task updatedTask) {
        // на всякий случай, защита от дурака
        if (updatedTask instanceof Subtask) {
            return updateSubtask((Subtask) updatedTask);
        } else if (updatedTask instanceof Epic) {
            return updateEpic((Epic) updatedTask);
        } else {
            Task tempLink = tasks.get(updatedTask.getId());
            tempLink.setName(updatedTask.getName());
            tempLink.setDescription(updatedTask.getDescription());
            tempLink.updateState(updatedTask.getState());
            return tempLink.getId();
        }
    }

    @Override
    public Subtask deleteSubtaskById(int id) {
        Subtask tempLink = subtasks.remove(id);
        tempLink.getParent().removeSubtask(tempLink);
        return tempLink;
    }

    @Override
    public Epic deleteEpicById(int id) {
        Epic tempLink = epics.remove(id);
        for (Subtask subtask : tempLink.getSubtasks()) {
            deleteSubtaskById(subtask.getId());
        }
        return tempLink;
    }

    @Override
    public Task deleteTaskById(int id) {
        Task tempLink = tasks.remove(id);
        return tempLink;
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<Task>(historyManager.getViewedTaskHistory());
    }
}
