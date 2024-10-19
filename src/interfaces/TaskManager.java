package interfaces;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    Set<Task> getPrioritizedTasks();

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

    void updateTask(Epic updatedEpic);

    void updateTask(Subtask updatedSubtask);

    void updateTask(Task updatedTask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getHistory();
}
