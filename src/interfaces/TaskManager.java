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

    int addNewTask(Epic newEpic);

    int addNewTask(Subtask newSubtask);

    int addNewTask(Task newTask);

    int updateTask(Epic updatedEpic);


    int updateTask(Subtask updatedSubtask);

    int updateTask(Task updatedTask);

    Subtask deleteSubtaskById(int id);

    Epic deleteEpicById(int id);

    Task deleteTaskById(int id);

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getHistory();
}
