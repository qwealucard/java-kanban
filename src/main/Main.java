package main;

import saves.TaskType;
import memory.InMemoryTaskManager;
import states.TaskState;
import tasks.*;
import interfaces.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task(0, TaskType.TASK,"Name1", TaskState.NEW, "description 1");
        Epic epic1 = new Epic(1, TaskType.EPIC, "Epic1", TaskState.NEW, "description 2" );

        TaskManager manager = new InMemoryTaskManager();
        manager.addNewTask(task1);
        manager.addNewTask(epic1);

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task epicsSubtasks : manager.getSubtasksByEpic((Epic) epic)) {
                System.out.println("--> " + epicsSubtasks);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks())
        {
            System.out.println(subtask);
        }

        System.out.println("История:");
        List<Task> hist = manager.getHistory();
        System.out.println(hist);
    }
}
