package test;

import savingfiles.ManagerSaveException;
import savingfiles.TaskType;
import interfaces.TaskManager;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    @Test
    public void testTaskImmutability() throws ManagerSaveException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task originalTask = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());

        taskManager.addNewTask(originalTask);

        Task taskInManager = taskManager.getTaskByID(originalTask.getId());

        Assertions.assertEquals(originalTask, taskInManager, "Задача в менеджере должна оставаться неизменной");
    }

    @Test
    public void testTaskIdConflict() throws ManagerSaveException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        Task taskWithSpecifiedId = new Task(0, TaskType.TASK, "Task with specified id", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task generatedIdTask =new Task(0, TaskType.TASK, "Task with generated id", TaskState.NEW, "Description 1", Duration.ofHours(2), startTime);

        taskManager.addNewTask(taskWithSpecifiedId);
        taskManager.addNewTask(generatedIdTask);

        assertNotEquals(taskWithSpecifiedId.getId(), generatedIdTask.getId());
    }

    @Test
    public void testAddAndFindTask() throws ManagerSaveException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(2), startTime);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        Task foundTask1 = taskManager.getTaskByID(task1.getId());
        Task foundTask2 = taskManager.getTaskByID(task2.getId());

        Assertions.assertEquals(task1, foundTask1, "Task 1 не найден по id");
        Assertions.assertEquals(task2, foundTask2, "Task 2 не наден по id");
    }

    @Test
    public void testGetTaskByID() throws ManagerSaveException {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        taskManager.addNewTask(task1);

        Task retrievedTask = taskManager.getTaskByID(0);

        assertNotNull(retrievedTask);
        Assertions.assertEquals(0, retrievedTask.getId(), "Идентификатор задачи должен совпадать");
        Assertions.assertEquals("Task 1", retrievedTask.getName(), "Название задачи должно совпадать");
        Assertions.assertEquals("Description 1", retrievedTask.getDescription(), "Описание задачи должно соответствовать");
        Assertions.assertEquals(TaskState.NEW, retrievedTask.getState(), "Состояние задачи должно соответствовать");
    }

    @Test
    public void testDeleteAllTasks() throws ManagerSaveException {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(2), startTime);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        taskManager.deleteAllTasks();

        Assertions.assertEquals(0, taskManager.getAllTasks().size(), "После удаления в диспетчере задач не должно быть никаких задач");
    }

    @Test
    public void testDeleteTaskById() throws ManagerSaveException {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        taskManager.addNewTask(task);

        taskManager.deleteTaskById(0);

        Assertions.assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void testIsOverlapping_OverlappingTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 12, 1, 11, 0, 0);

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(2), startTime1);
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), startTime2);

        assertTrue(taskManager.isOverlapping(task1, task2));
    }

    @Test
    void testIsOverlapping_NonOverlappingTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 12, 1, 12, 0, 0);

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(2), startTime1);
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), startTime2);

        assertFalse(taskManager.isOverlapping(task1, task2));
    }

    @Test
    void testGetPrioritizedTasks_EmptyTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0, prioritizedTasks.size());
    }

    @Test
    void testGetPrioritizedTasks_SingleTask() {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(2), startTime);
        taskManager.addNewTask(task);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(1, prioritizedTasks.size());
        assertEquals(task, prioritizedTasks.get(0));
    }

    @Test
    void testGetPrioritizedTasks_MultipleTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 12, 1, 11, 0, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2023, 12, 1, 12, 0, 0);

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime1);
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), startTime2);
        Task task3 = new Task(2, TaskType.TASK, "Task 3", TaskState.NEW, "Description 3", Duration.ofHours(3), startTime3);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size());
        assertEquals(task1, prioritizedTasks.get(0));
        assertEquals(task2, prioritizedTasks.get(1));
        assertEquals(task3, prioritizedTasks.get(2));
    }

    @Test
    void testGetPrioritizedTasks_EpicWithSubtasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 12, 1, 16, 0, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2023, 12, 1, 12, 0, 0);

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(2), startTime1);
        Epic epic = new Epic(1, TaskType.EPIC, "Epic 1", TaskState.NEW, "Epic Description", Duration.ZERO, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2, TaskType.SUBTASK, "Subtask 2", TaskState.NEW, "Description 2", Duration.ofHours(1), startTime2, 1);
        Subtask subtask2 = new Subtask(3, TaskType.SUBTASK, "Subtask 3", TaskState.NEW, "Description 3", Duration.ofHours(3), startTime3, 1);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size());
        assertEquals(task1, prioritizedTasks.get(0));
        assertEquals(subtask1, prioritizedTasks.get(2));
        assertEquals(subtask2, prioritizedTasks.get(1));
    }
}
