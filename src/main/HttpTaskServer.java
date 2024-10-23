package main;

import CustomTypeAdapter.DurationTypeAdapter;
import CustomTypeAdapter.LocalDateTimeAdapter;
import CustomTypeAdapter.TaskTypeAdapter;
import SerializeAndDeserialize.TaskDeserializer;
import SerializeAndDeserialize.TaskSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import history.InMemoryHistoryManager;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import memory.InMemoryTaskManager;
import savingfiles.ManagerSaveException;
import tasks.Task;
import utils.Manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    public static final String TASKS_PATH = "/tasks";
    public static final String SUBTASKS_PATH = "/subtasks";
    public static final String EPICS_PATH = "/epics";
    public static final String HISTORY_PATH = "/history";
    public static final String PRIORITIZED_PATH = "/prioritized";

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Task.class, new TaskSerializer()) // Регистрация сериализатора
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .create();
    public static Gson getGson() {
        return gson;
    }

    private static HttpServer server;
    private static TaskManager taskManager = new InMemoryTaskManager();
    private static HistoryManager historyManager = new InMemoryHistoryManager();

    // Конструктор
    public HttpTaskServer(TaskManager taskManager, HistoryManager historyManager) {
        this.taskManager =  taskManager;
        this.historyManager = historyManager;
    }

    // Метод для запуска сервера
    public static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext(TASKS_PATH, new TaskHandler(taskManager, gson));
        server.createContext(SUBTASKS_PATH, new SubtaskHandler(taskManager));
        server.createContext(EPICS_PATH, new EpicHandler(taskManager));
        server.createContext(HISTORY_PATH, new HistoryHandler(taskManager, historyManager));
        server.createContext(PRIORITIZED_PATH, new PrioritizedHandler(taskManager));

        server.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
        }
        System.out.println("Сервер остановлен.");
    }

    // Метод main
    public static void main(String[] args) throws ManagerSaveException, IOException {
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();
        HttpTaskServer server = new HttpTaskServer(taskManager, historyManager);
        server.start();
    }
}
