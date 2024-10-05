package interfaces;

import saving_files.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Task> getAllEpics();

    List<Task> getAllSubtasks();

    void deleteAllTasks() throws ManagerSaveException;

    void deleteAllSubtasks() throws ManagerSaveException;

    void deleteAllEpics() throws ManagerSaveException;

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    int addNewTask(Epic newEpic) throws ManagerSaveException;

    int addNewTask(Subtask newSubtask) throws ManagerSaveException;

    int addNewTask(Task newTask) throws ManagerSaveException;

    void updateTask(Epic updatedEpic) throws ManagerSaveException;

    void updateTask(Subtask updatedSubtask) throws ManagerSaveException;

    void updateTask(Task updatedTask) throws ManagerSaveException;

    void deleteTaskById(int id) throws ManagerSaveException;

    void deleteEpicById(int id) throws ManagerSaveException;

    void deleteSubtaskById(int id) throws ManagerSaveException;

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getHistory();
}
