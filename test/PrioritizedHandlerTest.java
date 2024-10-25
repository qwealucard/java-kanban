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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PrioritizedHandlerTest {

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
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 12, 1, 10, 0, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2025, 12, 1, 10, 0, 0);

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime1);
        Task task2 = new Task(0, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(2), startTime2);
        Task task3 = new Task(0, TaskType.TASK, "Task 3", TaskState.NEW, "Description 3", Duration.ofHours(3), startTime3);
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(url)
                                         .GET()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(3, tasks.size(), "Неверное количество задач в списке");
        assertEquals(task1.getName(), tasks.get(0).getName(), "Первая задача должна иметь высокий приоритет");
        assertEquals(task2.getName(), tasks.get(1).getName(), "Вторая задача должна иметь средний приоритет");
        assertEquals(task3.getName(), tasks.get(2).getName(), "Третья задача должна иметь низкий приоритет");
    }
}
