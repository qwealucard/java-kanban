package main;
import memory.InMemoryTaskManager;
import states.TaskState;
import tasks.*;
import interface_class.*;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("Name1", "test", TaskState.NEW);
        Task task1 = new Task("Name2", "test", TaskState.NEW);
        Epic epic1 = new Epic("Name", "test", TaskState.NEW);
        Subtask subtask1 = new Subtask("Name", "test", TaskState.NEW, epic1);
        TaskManager manager = new InMemoryTaskManager();
        manager.addNewTask(task);
        manager.addNewTask(task1);
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1);
        manager.getTaskByID(1);
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
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
