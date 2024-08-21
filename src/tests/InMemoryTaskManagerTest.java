package tests;

import interface_class.TaskManager;
import memory.InMemoryTaskManager;
import org.junit.Test;
import states.TaskState;
import tasks.Task;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {
    @Test
    public void testTaskImmutability() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task originalTask = new Task("Task", "Description", TaskState.NEW);

        taskManager.addNewTask(originalTask);

        Task taskInManager = taskManager.getTaskByID(originalTask.getId());

        assertEquals("Задача в менеджере должна оставаться неизменной", originalTask, taskInManager);
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

        assertEquals("Task 1 не найден по id", task1, foundTask1);
        assertEquals("Task 2 не наден по id", task2, foundTask2);
    }

    @Test
    public void testGetTaskByID() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        taskManager.addNewTask(task1);

        Task retrievedTask = taskManager.getTaskByID(1);

        assertNotNull(retrievedTask);
        assertEquals("Идентификатор задачи должен совпадать", 1, retrievedTask.getId());
        assertEquals("Название задачи должно совпадать", "Task 1", retrievedTask.getName());
        assertEquals("Описание задачи должно соответствовать", "Description 1", retrievedTask.getDescription());
        assertEquals("Состояние задачи должно соответствовать", TaskState.NEW, retrievedTask.getState());
    }

    @Test
    public void testDeleteAllTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.addNewTask(new Task("Task 1", "Description 1", TaskState.NEW));
        taskManager.addNewTask(new Task("Task 2", "Description 2", TaskState.IN_PROGRESS));

        taskManager.deleteAllTasks();

        assertEquals("После удаления в диспетчере задач не должно быть никаких задач", 0, taskManager.getAllTasks().size());
    }
}
