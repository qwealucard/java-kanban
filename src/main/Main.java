package main;

import savingfiles.ManagerSaveException;
import savingfiles.TaskType;
import memory.InMemoryTaskManager;
import states.TaskState;
import tasks.*;
import interfaces.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        Task task1 = new Task(0, TaskType.TASK,"Name1", TaskState.NEW, "description 1", Duration.ofHours(1), LocalDateTime.now());
        Epic epic1 = new Epic(1, TaskType.EPIC, "Epic1", TaskState.NEW, "description 2", Duration.ofHours(1), LocalDateTime.now());

        TaskManager manager = new InMemoryTaskManager();
        manager.addNewTask(task1);
        manager.addNewTask(epic1);

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        manager.getAllTasks().stream().forEach(System.out::println);

        System.out.println("Эпики:");
        manager.getAllEpics().stream().forEach(epic -> {
            System.out.println(epic);
            manager.getSubtasksByEpic((Epic) epic).stream().forEach(subtask -> System.out.println("--> " + subtask));
        });

        System.out.println("Подзадачи:");
        manager.getAllSubtasks().stream().forEach(System.out::println);

        System.out.println("История:");
        List<Task> hist = manager.getHistory();
        System.out.println(hist);
    }
}
