package taskmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tasks.*;
public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int currentId = 0;

    public TaskManager() {
    }

    public List<Task> getAllTasks() {
        return new ArrayList(tasks.values());
    }

    public List<Task> getAllEpics() {
        return new ArrayList(epics.values());
    }

    public List<Task> getAllSubtasks() {
        return new ArrayList(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
    }

    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
    }

    public Task getTaskByID(int id) {
        return tasks.get(id);
    }

    public Epic getEpicByID(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskByID(int id) {
        return subtasks.get(id);
    }

    public int addNewEpic(Epic newEpic) {
        newEpic.setId(currentId);
        epics.put(newEpic.getId(), newEpic);
        currentId++;
        return newEpic.getId();
    }

    public int addNewSubtask(Subtask newSubtask) {
        newSubtask.setId(currentId);
        newSubtask.getParent().addSubtask(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        currentId++;
        return newSubtask.getId();
    }

    public int addNewTask(Task newTask) {
        // на всякий случай, защита от дурака
        if (newTask instanceof Subtask) {
            return addNewSubtask((Subtask) newTask);
        } else if (newTask instanceof Epic) {
            return addNewEpic((Epic) newTask);
        } else {
            newTask.setId(currentId);
            tasks.put(newTask.getId(), newTask);
            currentId++;
            return newTask.getId();
        }
    }

    public int updateEpic(Epic updatedEpic) {
        Epic tempLink = epics.get(updatedEpic.getId());
        tempLink.setName(updatedEpic.getName());
        tempLink.setDescription(updatedEpic.getDescription());
        return tempLink.getId();
    }

    public int updateSubtask(Subtask updatedSubtask) {
        Subtask tempLink = subtasks.get(updatedSubtask.getId());
        tempLink.setName(updatedSubtask.getName());
        tempLink.setDescription(updatedSubtask.getDescription());
        tempLink.updateState(updatedSubtask.getState());
        return tempLink.getId();
    }

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

    public Subtask deleteSubtaskById(int id) {
        Subtask tempLink = subtasks.remove(id);
        tempLink.getParent().removeSubtask(tempLink);
        return tempLink;
    }

    public Epic deleteEpicById(int id) {
        Epic tempLink = epics.remove(id);
        for (Subtask subtask : tempLink.getSubtasks()) {
            deleteSubtaskById(subtask.getId());
        }
        return tempLink;
    }

    public Task deleteTaskById(int id) {
        Task tempLink = tasks.remove(id);
        return tempLink;
    }

    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }
}