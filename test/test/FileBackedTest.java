package test;

import SavedTask.FileBackedTaskManager;
import SavedTask.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTest {

    private File tempFile;
    private FileBackedTaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {
        tempFile.delete();
    }

    @Test
    public void testSaveMultipleTasks() throws IOException {
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2");

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        List<String> lines = Files.readAllLines(tempFile.toPath());

        assertEquals(2, lines.size());
    }

    @Test
    public void testSaveEmptyFile() throws IOException {
        taskManager.save();

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(0, lines.size());
    }

    @Test
    public void testLoadFromFile() {

        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        Epic epic = new Epic(1, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 2");
        Subtask subtask = new Subtask(2,TaskType.SUBTASK,"Subtask 1",TaskState.NEW,"Descripton 3", epic);

        taskManager.addNewTask(task);
        taskManager.addNewTask(epic);
        taskManager.addNewTask(subtask);

        List<String> lines = new ArrayList<>();
        lines.add("0,TASK,Task 1,NEW,Description 1");
        lines.add("1,EPIC,Epic 1,NEW,Description 2");
        lines.add("2,SUBTASK,Subtask 1,NEW,Description 3,epic");
        System.out.println("Содержимое файла: " + lines);

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        System.out.println(loadedTaskManager.getTasks().size());

        assertEquals(3, loadedTaskManager.getTasks().size());
        assertEquals(0, loadedTaskManager.getTasks().get(0).getId());
        assertEquals(TaskType.TASK, loadedTaskManager.getTasks().get(0).getType());
        assertEquals("Task 1", loadedTaskManager.getTasks().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getTasks().get(0).getState());
        assertEquals("Description 1", loadedTaskManager.getTasks().get(0).getDescription());
    }
    @Test
    public void testLoadEmptyFile() {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(0, loadedTaskManager.getTasks().size());
    }
}
