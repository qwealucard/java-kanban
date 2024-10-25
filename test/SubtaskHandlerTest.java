import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaces.TaskManager;
import main.HttpTaskServer;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import savingfiles.TaskType;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class SubtaskHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        manager.addNewTask(epic);
        Subtask subtask = new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0);
        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                                         .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        manager.addNewTask(epic);
        Subtask subtask = new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0);
        manager.addNewTask(subtask);
        int subtaskId = subtask.getId();

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .DELETE()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getAllSubtasks();
        assertEquals(0, tasks.size(), "Задача не удалена");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        manager.addNewTask(epic);
        Subtask subtask = new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0);
        manager.addNewTask(subtask);
        int subtaskId = subtask.getId();

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .GET()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Subtask receivedTask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask.getName(), receivedTask.getName(), "Полученная задача не совпадает с исходной");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        manager.addNewTask(epic);
        Subtask subtask = new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0);
        manager.addNewTask(subtask);
        int subtaskId = subtask.getId();
        subtask.setName("Updated Task");
        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        Subtask updatedTask = manager.getSubtaskByID(subtaskId);
        assertEquals("Updated Task", updatedTask.getName(), "Задача не обновлена");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2022, 12, 1, 10, 0, 0);

        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime2);
        manager.addNewTask(epic);
        Subtask subtask1 = new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime1, 0);
        Subtask subtask2 = new Subtask(1, TaskType.SUBTASK, "Subtask 2", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0);
        manager.addNewTask(subtask1);
        manager.addNewTask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .GET()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(200, response.statusCode());

        List<Subtask> tasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {}.getType());
        assertEquals(2, tasks.size(), "Некорректное количество задач в списке");
    }
}
