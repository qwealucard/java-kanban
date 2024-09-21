package main;

import memory.InMemoryTaskManager;
import states.TaskState;
import tasks.*;
import interfaces.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Name1", "test", TaskState.NEW);
        Task task2 = new Task("Name3", "test", TaskState.NEW);
        Task task3 = new Task("Name4", "test", TaskState.NEW);
        Task task4 = new Task("Name5", "test", TaskState.NEW);
        Task task5 = new Task("Name6", "test", TaskState.NEW);
        Task task6 = new Task("Name7", "test", TaskState.NEW);
        Task task7 = new Task("Name8", "test", TaskState.NEW);
        Task task8 = new Task("Name9", "test", TaskState.NEW);
        Task task9 = new Task("Name10", "test", TaskState.NEW);
        Task task10 = new Task("Name11", "test", TaskState.NEW);
        Task task11 = new Task("Name12", "test", TaskState.NEW);
        Epic epic1 = new Epic("Name", "test", TaskState.NEW);
        Subtask subtask1 = new Subtask("Name", "test", TaskState.NEW, epic1);
        TaskManager manager = new InMemoryTaskManager();
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);
        manager.addNewTask(task4);
        manager.addNewTask(task5);
        manager.addNewTask(task6);
        manager.addNewTask(task7);
        manager.addNewTask(task8);
        manager.addNewTask(task9);
        manager.addNewTask(task10);
        manager.addNewTask(task11);
        manager.addNewTask(epic1);
        manager.addNewTask(subtask1);
        manager.getTaskByID(1);
        manager.getTaskByID(2);
        manager.getTaskByID(3);
        manager.getTaskByID(4);
        manager.getTaskByID(5);
        manager.getTaskByID(6);
        manager.getTaskByID(7);
        manager.getTaskByID(8);
        manager.getTaskByID(9);
        manager.getTaskByID(10);
        manager.getTaskByID(11);
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
        for (Task subtask : manager.getAllSubtasks()
        ) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        List<Task> hist = manager.getHistory();
        System.out.println(hist);
    }
}
