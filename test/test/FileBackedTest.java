package test;

import savingfiles.FileBackedTaskManager;
import savingfiles.ManagerSaveException;
import savingfiles.TaskType;
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

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {
        tempFile.delete();
    }

    @Test
    public void testSaveMultipleTasks() throws ManagerSaveException, IOException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2");

        loadedTaskManager.addNewTask(task1);
        loadedTaskManager.addNewTask(task2);

        List<String> lines = Files.readAllLines(tempFile.toPath());

        assertEquals(2, lines.size());
    }

    @Test
    public void testSaveEmptyFile() throws IOException, ManagerSaveException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        loadedTaskManager.save();

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(0, lines.size());
    }

    @Test
    public void testLoadFromFileTask() throws ManagerSaveException, IOException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        loadedTaskManager.addNewTask(task);

        List<String> lines = new ArrayList<>();
        lines.add("0,TASK,Task 1,NEW,Description 1");
        Files.write(tempFile.toPath(), lines);

        assertEquals(1, loadedTaskManager.getAllTasks().size());
        assertEquals(0, loadedTaskManager.getAllTasks().get(0).getId());
        assertEquals(TaskType.TASK, loadedTaskManager.getAllTasks().get(0).getType());
        assertEquals("Task 1", loadedTaskManager.getAllTasks().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllTasks().get(0).getState());
        assertEquals("Description 1", loadedTaskManager.getAllTasks().get(0).getDescription());
    }

    @Test
    public void testLoadFromFileEpic() throws ManagerSaveException, IOException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 2");
        loadedTaskManager.addNewTask(epic);

        List<String> lines = new ArrayList<>();
        lines.add("1,EPIC,Epic 1,NEW,Description 2");
        Files.write(tempFile.toPath(), lines);

        assertEquals(1, loadedTaskManager.getAllEpics().size());
        assertEquals(0, loadedTaskManager.getAllEpics().get(0).getId());
        assertEquals(TaskType.EPIC, loadedTaskManager.getAllEpics().get(0).getType());
        assertEquals("Epic 1", loadedTaskManager.getAllEpics().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllEpics().get(0).getState());
        assertEquals("Description 2", loadedTaskManager.getAllEpics().get(0).getDescription());
    }

    @Test
    public void testLoadFromFileSubtask() throws ManagerSaveException, IOException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 2");
        Subtask subtask = new Subtask(1, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 3", epic);
        loadedTaskManager.addNewTask(epic);
        loadedTaskManager.addNewTask(subtask);

        List<String> lines = new ArrayList<>();
        lines.add("1,EPIC,Epic 1,NEW,Description 2");
        lines.add("2,SUBTASK,Subtask 1,NEW,Description 3,epic");
        Files.write(tempFile.toPath(), lines);

        assertEquals(1, loadedTaskManager.getAllEpics().size());
        assertEquals(0, loadedTaskManager.getAllEpics().get(0).getId());
        assertEquals(TaskType.EPIC, loadedTaskManager.getAllEpics().get(0).getType());
        assertEquals("Epic 1", loadedTaskManager.getAllEpics().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllEpics().get(0).getState());
        assertEquals("Description 2", loadedTaskManager.getAllEpics().get(0).getDescription());
        assertEquals(1, loadedTaskManager.getAllSubtasks().get(0).getId());
        assertEquals(TaskType.SUBTASK, loadedTaskManager.getAllSubtasks().get(0).getType());
        assertEquals("Subtask 1", loadedTaskManager.getAllSubtasks().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllSubtasks().get(0).getState());
        assertEquals("Description 3", loadedTaskManager.getAllSubtasks().get(0).getDescription());
    }

    @Test
    public void testLoadFromFileMultipleTasks() throws ManagerSaveException, IOException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        Epic epic = new Epic(1, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 2");
        Subtask subtask = new Subtask(2, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 3", epic);

        loadedTaskManager.addNewTask(task);
        loadedTaskManager.addNewTask(epic);
        loadedTaskManager.addNewTask(subtask);

        List<String> lines = new ArrayList<>();
        lines.add("0,TASK,Task 1,NEW,Description 1");
        lines.add("1,EPIC,Epic 1,NEW,Description 2");
        lines.add("2,SUBTASK,Subtask 1,NEW,Description 3,epic");
        Files.write(tempFile.toPath(), lines);

        assertEquals(1, loadedTaskManager.getAllTasks().size());
        assertEquals(1, loadedTaskManager.getAllEpics().size());
        assertEquals(1, loadedTaskManager.getAllSubtasks().size());
        assertEquals(0, loadedTaskManager.getAllTasks().get(0).getId());
        assertEquals(TaskType.TASK, loadedTaskManager.getAllTasks().get(0).getType());
        assertEquals("Task 1", loadedTaskManager.getAllTasks().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllTasks().get(0).getState());
        assertEquals("Description 1", loadedTaskManager.getAllTasks().get(0).getDescription());
        assertEquals(1, loadedTaskManager.getAllEpics().get(0).getId());
        assertEquals(TaskType.EPIC, loadedTaskManager.getAllEpics().get(0).getType());
        assertEquals("Epic 1", loadedTaskManager.getAllEpics().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllEpics().get(0).getState());
        assertEquals("Description 2", loadedTaskManager.getAllEpics().get(0).getDescription());
        assertEquals(2, loadedTaskManager.getAllSubtasks().get(0).getId());
        assertEquals(TaskType.SUBTASK, loadedTaskManager.getAllSubtasks().get(0).getType());
        assertEquals("Subtask 1", loadedTaskManager.getAllSubtasks().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllSubtasks().get(0).getState());
        assertEquals("Description 3", loadedTaskManager.getAllSubtasks().get(0).getDescription());
    }

    @Test
    public void testLoadEmptyFile() throws ManagerSaveException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(0, loadedTaskManager.getAllTasks().size());
        assertEquals(0, loadedTaskManager.getAllEpics().size());
        assertEquals(0, loadedTaskManager.getAllSubtasks().size());
    }
}
