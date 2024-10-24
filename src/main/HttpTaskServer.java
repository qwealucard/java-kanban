package main;

import CustomTypeAdapter.DurationTypeAdapter;
import CustomTypeAdapter.LocalDateTimeAdapter;
import serializeanddeserialize.TaskDeserializer;
import serializeanddeserialize.TaskSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import interfaces.TaskManager;
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
            .registerTypeAdapter(Task.class, new TaskSerializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .create();

    public static Gson getGson() {
        return gson;
    }

    private static HttpServer server;
    private static TaskManager taskManager = Manager.getDefault();

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext(TASKS_PATH, new TaskHandler(taskManager));
        server.createContext(SUBTASKS_PATH, new SubtaskHandler(taskManager));
        server.createContext(EPICS_PATH, new EpicHandler(taskManager));
        server.createContext(HISTORY_PATH, new HistoryHandler(taskManager));
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

    public static void main(String[] args) throws ManagerSaveException, IOException {
        TaskManager taskManager = Manager.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}
