import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import customtypeadapter.DurationTypeAdapter;
import customtypeadapter.LocalDateTimeAdapter;
import interfaces.TaskManager;
import main.HttpTaskServer;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import savingfiles.TaskType;
import serializeanddeserialize.TaskDeserializer;
import serializeanddeserialize.TaskSerializer;
import states.TaskState;
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

public class TaskHandlerTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(8080,
            "/tasks",
            "/subtasks",
            "/epics",
            "/history",
            "/prioritized",
            manager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Task.class, new TaskSerializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .create();

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
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                                         .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Task 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        manager.addNewTask(task);

        int taskId = task.getId();

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .DELETE()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getAllTasks();
        assertEquals(0, tasks.size(), "Задача не удалена");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime);
        manager.addNewTask(task);

        int taskId = task.getId();

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .GET()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getName(), receivedTask.getName(), "Полученная задача не совпадает с исходной");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        manager.addNewTask(task);

        int taskId = task.getId();

        task.setName("Updated Task");
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        Task updatedTask = manager.getTaskByID(taskId);
        assertEquals("Updated Task", updatedTask.getName(), "Задача не обновлена");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime);
        Task task2 = new Task(0, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(2), LocalDateTime.now());
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .GET()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(2, tasks.size(), "Некорректное количество задач в списке");
    }

    @Test
    public void testGetTaskById_NotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .GET()
                                         .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}