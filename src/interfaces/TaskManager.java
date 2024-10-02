package interfaces;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Task> getAllEpics();

    List<Task> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    void addNewTask(Epic newEpic);

    void addNewTask(Subtask newSubtask);

    void addNewTask(Task newTask);

    void updateTask(Epic updatedEpic);

    void updateTask(Subtask updatedSubtask);

    void updateTask(Task updatedTask);

    void deleteTaskById(int id);

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getHistory();
}
