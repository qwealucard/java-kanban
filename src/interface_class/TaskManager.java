package interface_class;

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

    int addNewEpic(Epic newEpic);

    int addNewSubtask(Subtask newSubtask);

    int addNewTask(Task newTask);

    int updateEpic(Epic updatedEpic);

    int updateSubtask(Subtask updatedSubtask);

    int updateTask(Task updatedTask);

    Subtask deleteSubtaskById(int id);

    Epic deleteEpicById(int id);

    Task deleteTaskById(int id);

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getHistory();
}
