package test;

import SavedTask.FileBackedTaskManager;
import SavedTask.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
       // File file = File.createTempFile("test", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2");

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        List<String> lines = Files.readAllLines(tempFile.toPath());
        System.out.println(lines);
        assertEquals(2, lines.size());
    }

    @Test
    public void testSaveEmptyFile() throws IOException {
       // File file = File.createTempFile("test", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        taskManager.save();

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(0, lines.size());
    }

    @Test
    public void testLoadFromFile() throws IOException {

        //File testFile = File.createTempFile("test", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");

        taskManager.addNewTask(task);


        List<String> lines = new ArrayList<>();
        lines.add("0,TASK,Task 1,NEW,Description 1");
        try {
            Files.write(tempFile.toPath(), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        System.out.println(loadedTaskManager.getTasks().size());

        assertEquals(1, loadedTaskManager.getTasks().size());
        //assertEquals("Task 1", loadedTaskManager.getAllTasks().get(0).getName());
    }
}
