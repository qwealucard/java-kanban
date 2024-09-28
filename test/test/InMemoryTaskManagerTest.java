package test;

import interfaces.TaskManager;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {
    @Test
    public void testTaskImmutability() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task originalTask = new Task("Task", "Description", TaskState.NEW);

        taskManager.addNewTask(originalTask);

        Task taskInManager = taskManager.getTaskByID(originalTask.getId());

        Assertions.assertEquals(originalTask, taskInManager, "Задача в менеджере должна оставаться неизменной");
    }

    @Test
    public void testTaskIdConflict() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        String specifiedId = "specifiedId";
        Task taskWithSpecifiedId = new Task(specifiedId, "Task with specified id", TaskState.NEW);

        Task generatedIdTask = new Task("Task 2", "Task with generated id", TaskState.IN_PROGRESS);

        taskManager.addNewTask(taskWithSpecifiedId);
        taskManager.addNewTask(generatedIdTask);

        assertNotEquals(taskWithSpecifiedId.getId(), generatedIdTask.getId());
    }

    @Test
    public void testAddAndFindTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        Task foundTask1 = taskManager.getTaskByID(task1.getId());
        Task foundTask2 = taskManager.getTaskByID(task2.getId());

        Assertions.assertEquals(task1, foundTask1, "Task 1 не найден по id");
        Assertions.assertEquals(task2, foundTask2, "Task 2 не наден по id");
    }

    @Test
    public void testGetTaskByID() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        taskManager.addNewTask(task1);

        Task retrievedTask = taskManager.getTaskByID(0);

        assertNotNull(retrievedTask);
        Assertions.assertEquals(0, retrievedTask.getId(), "Идентификатор задачи должен совпадать");
        Assertions.assertEquals("Task 1", retrievedTask.getName(), "Название задачи должно совпадать");
        Assertions.assertEquals("Description 1", retrievedTask.getDescription(), "Описание задачи должно соответствовать");
        Assertions.assertEquals(TaskState.NEW, retrievedTask.getState(), "Состояние задачи должно соответствовать");
    }

    @Test
    public void testDeleteAllTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        taskManager.deleteAllTasks();

        Assertions.assertEquals(0, taskManager.getAllTasks().size(), "После удаления в диспетчере задач не должно быть никаких задач");
    }
}
