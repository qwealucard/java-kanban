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
        loadedTaskManager.saveToFile();

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(0, lines.size());
    }

    @Test
    public void testLoadFromFileTask() throws ManagerSaveException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        loadedTaskManager.addNewTask(task);

        assertEquals(1, loadedTaskManager.getAllTasks().size());
        assertEquals(0, loadedTaskManager.getAllTasks().get(0).getId());
        assertEquals(TaskType.TASK, loadedTaskManager.getAllTasks().get(0).getType());
        assertEquals("Task 1", loadedTaskManager.getAllTasks().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllTasks().get(0).getState());
        assertEquals("Description 1", loadedTaskManager.getAllTasks().get(0).getDescription());
    }

    @Test
    public void testLoadFromFileEpic() throws ManagerSaveException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 2");
        loadedTaskManager.addNewTask(epic);

        assertEquals(1, loadedTaskManager.getAllEpics().size());
        assertEquals(0, loadedTaskManager.getAllEpics().get(0).getId());
        assertEquals(TaskType.EPIC, loadedTaskManager.getAllEpics().get(0).getType());
        assertEquals("Epic 1", loadedTaskManager.getAllEpics().get(0).getName());
        assertEquals(TaskState.NEW, loadedTaskManager.getAllEpics().get(0).getState());
        assertEquals("Description 2", loadedTaskManager.getAllEpics().get(0).getDescription());
    }

    @Test
    public void testLoadFromFileSubtask() throws ManagerSaveException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 2");
        Subtask subtask = new Subtask(1, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 3", 0);
        loadedTaskManager.addNewTask(epic);
        loadedTaskManager.addNewTask(subtask);

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
    public void testLoadFromSavingFileTask() throws ManagerSaveException, IOException {
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        List<String> lines = new ArrayList<>();
        lines.add(task.toString());
        Files.write(tempFile.toPath(), lines);

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedTaskManager.getAllTasks().size());
        assertEquals(task.getId(), loadedTaskManager.getAllTasks().get(0).getId());
        assertEquals(task.getType(), loadedTaskManager.getAllTasks().get(0).getType());
        assertEquals(task.getName(), loadedTaskManager.getAllTasks().get(0).getName());
        assertEquals(task.getState(), loadedTaskManager.getAllTasks().get(0).getState());
        assertEquals(task.getDescription(), loadedTaskManager.getAllTasks().get(0).getDescription());
    }

    @Test
    public void testLoadFromSavingFileEpic() throws ManagerSaveException, IOException {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1");
        List<String> lines = new ArrayList<>();
        lines.add(epic.toString());
        Files.write(tempFile.toPath(), lines);

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        System.out.println(loadedTaskManager.getAllEpics());

        assertEquals(epic.getId(), loadedTaskManager.getAllEpics().get(0).getId());
        assertEquals(epic.getType(), loadedTaskManager.getAllEpics().get(0).getType());
        assertEquals(epic.getName(), loadedTaskManager.getAllEpics().get(0).getName());
        assertEquals(epic.getState(), loadedTaskManager.getAllEpics().get(0).getState());
        assertEquals(epic.getDescription(), loadedTaskManager.getAllEpics().get(0).getDescription());
    }

    @Test
    public void testLoadFromSavingFileSubtask() throws ManagerSaveException, IOException {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1");
        Subtask subtask = new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", 0);
        List<String> lines = new ArrayList<>();
        lines.add(epic.toString());
        lines.add(subtask.toString());
        Files.write(tempFile.toPath(), lines);

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedTaskManager.getAllSubtasks().size());
        assertEquals(1, loadedTaskManager.getAllEpics().size());
        assertEquals(subtask.getId(), loadedTaskManager.getAllSubtasks().get(0).getId());
        assertEquals(subtask.getType(), loadedTaskManager.getAllSubtasks().get(0).getType());
        assertEquals(subtask.getName(), loadedTaskManager.getAllSubtasks().get(0).getName());
        assertEquals(subtask.getState(), loadedTaskManager.getAllSubtasks().get(0).getState());
        assertEquals(subtask.getDescription(), loadedTaskManager.getAllSubtasks().get(0).getDescription());
        assertEquals(subtask.getParentId(), loadedTaskManager.getAllEpics().get(0).getId());
    }

    @Test
    public void testLoadFromFileMultipleTasks() throws ManagerSaveException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        Epic epic = new Epic(1, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 2");
        Subtask subtask = new Subtask(2, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 3", 1);

        loadedTaskManager.addNewTask(task);
        loadedTaskManager.addNewTask(epic);
        loadedTaskManager.addNewTask(subtask);

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
